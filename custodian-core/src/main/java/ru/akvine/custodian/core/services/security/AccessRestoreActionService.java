package ru.akvine.custodian.core.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.constants.DBLockPrefixesConstants;
import ru.akvine.custodian.core.enums.ActionState;
import ru.akvine.custodian.core.exceptions.security.*;
import ru.akvine.custodian.core.repositories.entities.security.AccessRestoreActionEntity;
import ru.akvine.custodian.core.repositories.entities.security.OtpActionEntity;
import ru.akvine.custodian.core.repositories.entities.security.OtpInfo;
import ru.akvine.custodian.core.repositories.security.AccessRestoreActionRepository;
import ru.akvine.custodian.core.repositories.security.ActionRepository;
import ru.akvine.custodian.core.services.ClientService;
import ru.akvine.custodian.core.services.domain.ClientModel;
import ru.akvine.custodian.core.services.dto.security.access_restore.AccessRestoreActionRequest;
import ru.akvine.custodian.core.services.dto.security.access_restore.AccessRestoreActionResult;
import ru.akvine.custodian.core.utils.Asserts;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessRestoreActionService extends OtpActionService<AccessRestoreActionEntity> {
    @Value("${security.otp.action.lifetime.seconds}")
    private int otpActionLifetimeSeconds;
    @Value("${security.otp.max.invalid.attempts}")
    private int optMaxInvalidAttempts;
    @Value("${security.otp.max.new.generation.per.action}")
    private int otpMaxNewGenerationPerAction;

    private final AccessRestoreActionRepository accessRestoreActionRepository;
    private final ClientService clientService;

    public AccessRestoreActionResult startAccessRestore(AccessRestoreActionRequest accessRestoreActionRequest) {
        Asserts.isNotNull(accessRestoreActionRequest, "accessRestoreActionRequest is null");

        String login = accessRestoreActionRequest.getLogin();
        String sessionId = accessRestoreActionRequest.getSessionId();

        verifyNotBlocked(login);

        AccessRestoreActionEntity accessRestoreActionEntity = lockHelper.doWithLock(getLock(login), () -> {
            AccessRestoreActionEntity accessRestoreAction = accessRestoreActionRepository.findCurrentAction(login);
            if (accessRestoreAction == null) {
                return createNewActionAndSendOtp(login, sessionId);
            }
            // Действие просрочено
            if (accessRestoreAction.getOtpAction().isActionExpired()) {
                accessRestoreActionRepository.delete(accessRestoreAction);
                return createNewActionAndSendOtp(login, sessionId);
            }
            // Код уже был введен - все корректно
            if (isInFinalState(accessRestoreAction)) {
                return accessRestoreAction;
            }
            // Действие не просрочено и код еще годен
            if (accessRestoreAction.getOtpAction().isNotExpiredOtp()) {
                return accessRestoreAction;
            }
            // Код просрочен, но новый сгенерровать не можем - лимит исчерпан
            if (accessRestoreAction.getOtpAction().isNewOtpLimitReached()) {
                handleNoMoreNewOtp(accessRestoreAction);
                throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
            }

            // Действие не просрочено, но просрочен код, нужно сгенерировать новый - лимит еще есть
            return updateNewOtpAndSendToClient(accessRestoreAction);
        });

        return buildActionInfo(accessRestoreActionEntity);
    }

    public AccessRestoreActionResult generateNewOneTimePassword(AccessRestoreActionRequest request) {
        Asserts.isNotNull(request, "accessRestoreActionRequest is null");
        Asserts.isNotNull(request.getLogin(), "accessRestoreActionRequest.login is null");
        Asserts.isNotNull(request.getSessionId(), "accessRestoreActionRequest.sessionId is null");
        return generateNewOtp(request.getLogin());
    }

    public AccessRestoreActionResult checkOneTimePassword(AccessRestoreActionRequest actionRequest) {
        Asserts.isNotNull(actionRequest, "accessRestoreActionRequest is null");

        String login = actionRequest.getLogin();
        String otpValue = actionRequest.getOtp();

        AccessRestoreActionEntity accessRestoreActionEntity = lockHelper.doWithLock(getLock(login), () -> {
            AccessRestoreActionEntity accessRestoreAction = accessRestoreActionRepository.findCurrentAction(login);
            if (accessRestoreAction == null) {
                logger.info("Client tried to check otp of {}, but action is not started", getActionName());
                throw new ActionNotStartedException(String.format("Can't be check otp of %s, action not initiated!", getActionName()));
            }

            verifyNotBlocked(login);

            // Действие просрочено
            if (accessRestoreAction.getOtpAction().isActionExpired()) {
                logger.info("Client with email = {} tried to check otp {}, but action is expired", login, getActionName());
                accessRestoreActionRepository.delete(accessRestoreAction);
                throw new ActionNotStartedException(String.format("Can't check otp of %s, action not initiated", getActionName()));
            }

            // Код уже был введен - все корректно
            if (isInFinalState(accessRestoreAction)) {
                return accessRestoreAction;
            }

            if (noCurrentOtpInfoAvailable(accessRestoreAction)) {
                return accessRestoreAction;
            }

            // Действие не просрочено, но просрочен код
            if (accessRestoreAction.getOtpAction().isExpiredOtp()) {
                logger.info("Client with email = {} tried to check otp {}, but otp is expired! New otp left = {}", login,
                        getActionName(), accessRestoreAction.getOtpAction().getOtpValue());
                throw new OtpExpiredException(accessRestoreAction.getOtpAction().getOtpCountLeft());
            }

            // Действие не просрочено и код еще активен - проверяем
            if (accessRestoreAction.getOtpAction().isOtpValid(otpValue)) {
                logger.debug("Client with email = {} successfully passed otp for state = {}", login, accessRestoreAction.getState());
                return handleValidOtp(accessRestoreAction);
            }

            // Неверный ввод
            int otpInvalidAttemptsLeft = accessRestoreAction.getOtpAction().decrementInvalidAttemptsLeft();
            AccessRestoreActionEntity updatedPasswordChangeAction = accessRestoreActionRepository.save(accessRestoreAction);
            if (otpInvalidAttemptsLeft == 0) {
                handleNoMoreOtpInvalidAttemptsLeft(updatedPasswordChangeAction);
                throw new BlockedCredentialsException(login);
            }

            throw new OtpInvalidAttemptException(login, otpInvalidAttemptsLeft);
        });

        return buildActionInfo(accessRestoreActionEntity);
    }

    public ClientModel finishAccessRestore(AccessRestoreActionRequest actionRequest) {
        Asserts.isNotNull(actionRequest, "actionRestoreRequest is null");

        String login = actionRequest.getLogin();
        String password = actionRequest.getPassword();

        return lockHelper.doWithLock(getLock(login), () -> {
            AccessRestoreActionEntity accessRestoreAction = accessRestoreActionRepository.findCurrentAction(login);
            if (accessRestoreAction == null) {
                logger.info("Client tried to finish {}, but action is not started", getActionName());
                throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated", getActionName()));
            }

            verifyNotBlocked(login);

            // Действие просрочено
            if (accessRestoreAction.getOtpAction().isActionExpired()) {
                logger.info("Client with email = {} tried to finish {}, but action is expired!", login, getActionName());
                accessRestoreActionRepository.delete(accessRestoreAction);
                logger.trace("Expired {}[id={}] removed from DB", getActionName(), accessRestoreAction.getId());
                throw new ActionNotStartedException(String.format("Can't finish %s, action not initiated", getActionName()));
            }

            if (accessRestoreAction.getState() == ActionState.OTP_PASSED) {
                accessRestoreActionRepository.delete(accessRestoreAction);
                return clientService.updatePassword(login, password);
            }

            throw new OtpAuthRequiredException(String.format("Can't finish %s, otp auth required!", getActionName()));
        });
    }

    @Override
    protected String getActionName() {
        return "access-restore-action";
    }

    @Override
    protected String getLock(String payload) {
        return DBLockPrefixesConstants.ACCESS_RESTORE_PREFIX + payload;
    }

    @Override
    protected ActionRepository<AccessRestoreActionEntity> getRepository() {
        return accessRestoreActionRepository;
    }

    @Override
    protected void sendNewOtpToClient(AccessRestoreActionEntity action) {
        String login = action.getLogin();
        String otpValue = action.getOtpAction().getOtpValue();
        Integer otpNumber = action.getOtpAction().getOtpNumber();

        notificationProvider.sendAccessRestoreCode(login, otpValue);
        logger.info("Otp №{} for {} has been send to client with email = {}", otpNumber, getActionName(), login);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected AccessRestoreActionResult buildActionInfo(AccessRestoreActionEntity action) {
        return new AccessRestoreActionResult(action);
    }

    private AccessRestoreActionEntity createNewActionAndSendOtp(String login, String sessionId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actionExpiredAt = now.plusSeconds(otpActionLifetimeSeconds);

        OtpInfo otpInfo = otpService.getOneTimePassword(login);
        OtpActionEntity otpAction = new OtpActionEntity()
                .setStartedDate(now)
                .setActionExpiredAt(actionExpiredAt)
                .setOtpInvalidAttemptsLeft(optMaxInvalidAttempts)
                .setOtpCountLeft(otpMaxNewGenerationPerAction)
                .setNewOtpValue(otpInfo, now);

        AccessRestoreActionEntity accessRestoreAction = new AccessRestoreActionEntity()
                .setLogin(login)
                .setSessionId(sessionId)
                .setState(ActionState.NEW)
                .setOtpAction(otpAction);
        notificationProvider.sendAccessRestoreCode(login, accessRestoreAction.getOtpAction().getOtpValue());
        logger.info("Client with email = {} started new {}", login, getActionName());
        return accessRestoreActionRepository.save(accessRestoreAction);
    }

    private AccessRestoreActionEntity handleValidOtp(AccessRestoreActionEntity accessRestoreAction) {
        accessRestoreAction.setState(ActionState.OTP_PASSED);
        accessRestoreAction.getOtpAction().setOtpValueToNull();
        return accessRestoreActionRepository.save(accessRestoreAction);
    }

    private boolean isInFinalState(AccessRestoreActionEntity accessRestoreAction) {
        return accessRestoreAction.getState() == ActionState.OTP_PASSED;
    }
}
