package ru.akvine.custodian.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.custodian.core.enums.NotificationProviderType;
import ru.akvine.custodian.core.services.notification.NotificationProvider;
import ru.akvine.custodian.core.services.notification.dummy.DummyNotificationProvider;
import ru.akvine.custodian.core.services.notification.dummy.LogNotificationService;
import ru.akvine.custodian.core.validators.NotificationValidator;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Configuration
public class NotificationConfig {
    @Value("${security.dummy.notification.provider.enabled}")
    private boolean dummyProviderEnabled;
    @Value("${security.notification.provider.type}")
    private String notificationProviderType;

    private final Map<NotificationProviderType, NotificationProvider> availableNotificationProviders;
    private final NotificationValidator notificationValidator;

    @Autowired
    public NotificationConfig(List<NotificationProvider> notificationProviders,
                              NotificationValidator notificationValidator) {
        this.notificationValidator = notificationValidator;
        this.availableNotificationProviders = notificationProviders
                .stream()
                .collect(toMap(NotificationProvider::getType, identity()));
    }

    @Bean
    public NotificationProvider notificationProvider() {
        notificationValidator.validate(notificationProviderType);
        NotificationProviderType type = NotificationProviderType.valueOf(notificationProviderType);

        NotificationProvider notificationProvider = availableNotificationProviders.get(type);
        if (dummyProviderEnabled && notificationProvider instanceof DummyNotificationProvider) {
            return notificationProvider;
        }
        if (!dummyProviderEnabled && notificationProvider instanceof DummyNotificationProvider) {
            throw new IllegalStateException("Dummy notification provider can't be used in non-testing environment!");
        }

        if (dummyProviderEnabled && !(notificationProvider instanceof DummyNotificationProvider)) {
            throw new IllegalStateException("Real notification provider can't be used in testing environment!");
        }

        return new LogNotificationService();
    }
}