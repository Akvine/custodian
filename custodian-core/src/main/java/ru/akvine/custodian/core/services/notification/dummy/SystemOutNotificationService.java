package ru.akvine.custodian.core.services.notification.dummy;

import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.enums.NotificationProviderType;

@Service
public class SystemOutNotificationService implements DummyNotificationProvider {
    @Override
    public boolean sendRegistrationCode(String login, String code) {
        String message = String.format("Successful send registration code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendAuthenticationCode(String login, String code) {
        String message = String.format("Successful send auth code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendAccessRestoreCode(String login, String code) {
        String message = String.format("Successful send access restore code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendProfileDeleteCode(String login, String code) {
        String message = String.format("Successful send profile delete code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendProfileChangeEmailCode(String login, String code) {
        String message = String.format("Successful send profile change email code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendProfileChangePasswordCode(String login, String code) {
        String message = String.format("Successful send profile change password code = [%s] to login = [%s]", code, login);
        System.out.println(message);
        return true;
    }

    @Override
    public NotificationProviderType getType() {
        return NotificationProviderType.SYSTEM_OUT;
    }
}
