package ru.akvine.custodian.core.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.custodian.core.repositories.entities.security.OtpCounterEntity;
import ru.akvine.custodian.core.repositories.entities.security.OtpInfo;
import ru.akvine.custodian.core.repositories.security.OtpCounterRepository;
import ru.akvine.custodian.core.services.notification.NotificationProvider;
import ru.akvine.custodian.core.services.notification.dummy.ConstantNotificationService;
import ru.akvine.custodian.core.utils.Asserts;
import ru.akvine.custodian.core.utils.OneTimePasswordGenerator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    private final NotificationProvider notificationProvider;
    private final OtpCounterRepository otpCounterRepository;

    private static final String DUMMY_CODE = "1";

    @Value("${security.dummy.notification.provider.enabled}")
    private boolean dummyEnabled;
    @Value("${security.otp.length}")
    private int otpLength;
    @Value("${security.otp.max.invalid.attempts}")
    private int otpMaxInvalidAttempts;
    @Value("${security.otp.lifetime.seconds}")
    private long otpLifetimeSeconds;

    public OtpInfo getOneTimePassword(String login) {
        Asserts.isNotNull(login, "login is null");

        String value;
        if (dummyEnabled && notificationProvider instanceof ConstantNotificationService) {
            value = StringUtils.repeat(DUMMY_CODE, otpLength);
        } else {
            value = OneTimePasswordGenerator.generate(otpLength);
        }
        int orderNumber = (int) getNextOtpNumber(login);
        logger.info("Otp №{} has been generated for client with email = {}", orderNumber, login);
        return new OtpInfo(
                otpMaxInvalidAttempts,
                otpLifetimeSeconds,
                orderNumber,
                value);
    }

    @Transactional
    public long getNextOtpNumber(String login) {
        Asserts.isNotNull(login, "login is null");

        LocalDateTime now = LocalDateTime.now();
        OtpCounterEntity otpCounter = otpCounterRepository.findByLogin(login);
        if (otpCounter == null) {
            otpCounter = otpCounterRepository.save(
                    new OtpCounterEntity()
                            .setLogin(login)
                            .setLastUpdated(now)
                            .setValue(1)
            );
            return otpCounter.getValue();
        }

        LocalDateTime lastUpdate = otpCounter.getLastUpdated();
        if (isTimeToReset(lastUpdate, now)) {
            otpCounter
                    .setLastUpdated(now)
                    .setValue(1);
            otpCounter = otpCounterRepository.save(otpCounter);
            return otpCounter.getValue();
        }

        long nextValue = otpCounter.getValue() + 1;
        otpCounter
                .setLastUpdated(now)
                .setValue(nextValue);
        otpCounter = otpCounterRepository.save(otpCounter);
        return otpCounter.getValue();
    }

    public static boolean isTimeToReset(LocalDateTime lastUpdate, LocalDateTime now) {
        boolean oneDayPassed = lastUpdate.until(now, ChronoUnit.DAYS) >= 1;
        boolean nextDayHasCome = lastUpdate.getDayOfYear() != now.getDayOfYear();
        return oneDayPassed || nextDayHasCome;
    }
}
