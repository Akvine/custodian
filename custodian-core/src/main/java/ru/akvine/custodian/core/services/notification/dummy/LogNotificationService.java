package ru.akvine.custodian.core.services.notification.dummy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.enums.NotificationProviderType;

@Service
@Slf4j
public class LogNotificationService implements DummyNotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        logger.info("Successful send registration code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        logger.info("Successful send authentication code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public boolean sendAccessRestoreCode(String login, String code) {
        logger.info("Successful send access restore code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public boolean sendProfileDeleteCode(String login, String code) {
        logger.info("Successful send delete profile code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public boolean sendProfileChangeEmailCode(String login, String code) {
        logger.info("Successful send profile change email code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public boolean sendProfileChangePasswordCode(String login, String code) {
        logger.info("Successful send profile change password code = [" + code + "] to login = [" + login + "]");
        return true;
    }

    @Override
    public NotificationProviderType getType() {
        return NotificationProviderType.LOG;
    }
}
