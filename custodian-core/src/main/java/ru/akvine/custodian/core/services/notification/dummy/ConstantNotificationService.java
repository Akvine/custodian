package ru.akvine.custodian.core.services.notification.dummy;

import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.enums.NotificationProviderType;

@Service
public class ConstantNotificationService implements DummyNotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendAccessRestoreCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendProfileDeleteCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendProfileChangeEmailCode(String login, String code) {
        return true;
    }

    @Override
    public boolean sendProfileChangePasswordCode(String login, String code) {
        return true;
    }

    @Override
    public NotificationProviderType getType() {
        return NotificationProviderType.CONSTANT;
    }
}
