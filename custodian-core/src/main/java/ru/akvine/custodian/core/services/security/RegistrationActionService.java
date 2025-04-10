package ru.akvine.custodian.core.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.constants.DBLockPrefixesConstants;
import ru.akvine.custodian.core.enums.ActionState;
import ru.akvine.custodian.core.exceptions.security.NoMoreNewOtpAvailableException;
import ru.akvine.custodian.core.exceptions.security.RegistrationNotStartedException;
import ru.akvine.custodian.core.exceptions.security.RegistrationWrongStateException;
import ru.akvine.custodian.core.repositories.entities.security.OtpActionEntity;
import ru.akvine.custodian.core.repositories.entities.security.RegistrationActionEntity;
import ru.akvine.custodian.core.repositories.security.ActionRepository;
import ru.akvine.custodian.core.repositories.security.RegistrationActionRepository;
import ru.akvine.custodian.core.services.ClientService;
import ru.akvine.custodian.core.services.domain.ClientModel;
import ru.akvine.custodian.core.services.dto.client.ClientCreate;
import ru.akvine.custodian.core.services.dto.security.registration.RegistrationActionRequest;
import ru.akvine.custodian.core.services.dto.security.registration.RegistrationActionResult;
import ru.akvine.custodian.core.utils.Asserts;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationActionService extends OtpActionService<RegistrationActionEntity> {

        private final RegistrationActionRepository registrationActionRepository;
        private final ClientService clientService;

        @Value("${security.otp.action.lifetime.seconds}")
        private int otpActionLifetimeSeconds;
        @Value("${security.otp.max.invalid.attempts}")
        private int optMaxInvalidAttempts;
        @Value("${security.otp.max.new.generation.per.action}")
        private int otpMaxNewGenerationPerAction;

        public RegistrationActionResult startRegistration(RegistrationActionRequest request) {
            Asserts.isNotNull(request, "registrationActionRequest is null");

            String login = request.getEmail();
            String sessionId = request.getSessionId();

            verifyNotBlocked(login);

            RegistrationActionEntity registrationActionEntity = lockHelper.doWithLock(getLock(login), () -> {
                RegistrationActionEntity registrationAction = getRepository().findCurrentAction(login);
                if (registrationAction == null) {
                    return createNewActionAndSendOtp(login, sessionId);
                }
                // Действие просрочено
                if (registrationAction.getOtpAction().isActionExpired()) {
                    getRepository().delete(registrationAction);
                    return createNewActionAndSendOtp(login, sessionId);
                }
                // Действие не просрочено и код еще годе ... вернем текущее состояние
                if (registrationAction.getOtpAction().getOtpExpiredAt() != null && registrationAction.getOtpAction().isNotExpiredOtp()) {
                    return registrationAction;
                }
                // Пользователь не захотел вводить пароль, OTP уже было использовано, генерируем новый код
                if (registrationAction.getOtpAction().getOtpExpiredAt() == null) {
                    getRepository().delete(registrationAction);
                    return createNewActionAndSendOtp(login, sessionId);
                }
                // Код просрочен, но новый сгенерировать не можем - лимит исчерпан
                if (registrationAction.getOtpAction().isNewOtpLimitReached()) {
                    handleNoMoreNewOtp(registrationAction);
                    throw new NoMoreNewOtpAvailableException("No more one-time-password can be generated!");
                }

                // Действие не просрочено, но просрочен код, нужно сгенерировать нвоый - лимит еще есть
                return updateNewOtpAndSendToClient(registrationAction);
            });

            return buildActionInfo(registrationActionEntity);
        }

        public RegistrationActionResult checkOneTimePassword(RegistrationActionRequest request) {
            Asserts.isNotNull(request, "registrationAction is null");

            String login = request.getEmail();
            String sessionId = request.getSessionId();
            String otp = request.getOtp();

            Asserts.isNotNull(login, "login is null");
            Asserts.isNotNull(sessionId, "sessionId is null");
            Asserts.isNotNull(otp, "otp is null");

            verifyNotBlocked(login);

            RegistrationActionEntity registrationActionEntity = lockHelper.doWithLock(getLock(login), () -> {
                RegistrationActionEntity registrationAction = checkOtpInput(login, otp, sessionId);
                registrationAction.setState(ActionState.OTP_PASSED);
                registrationAction.getOtpAction().setOtpValueToNull();

                RegistrationActionEntity savedRegistrationAction = getRepository().save(registrationAction);
                logger.info("Client with email = {} was successfully passed otp!", login);
                return  savedRegistrationAction;
            });

            return buildActionInfo(registrationActionEntity);
        }

        public RegistrationActionResult generateNewOneTimePassword(RegistrationActionRequest request) {
            Asserts.isNotNull(request, "registrationActionRequest is null");
            Asserts.isNotNull(request.getEmail(), "registrationActionRequest.email is null");
            Asserts.isNotNull(request.getSessionId(), "registrationActionRequest.sessionId is null");
            return generateNewOtp(request.getEmail());
        }

        public ClientModel finishRegistration(RegistrationActionRequest request) {
            Asserts.isNotNull(request, "registrationActionRequest is null");

            String login = request.getEmail();
            verifyNotBlocked(login);

            return lockHelper.doWithLock(getLock(login), () -> {
                RegistrationActionEntity registrationAction = getRepository().findCurrentAction(login);
                if (registrationAction == null) {
                    logger.info("Registration for email = {} not started yet!", login);
                    throw new RegistrationNotStartedException("Registration not started yet");
                }

                // Действие просрочено
                if (registrationAction.getOtpAction().isActionExpired()) {
                    getRepository().delete(registrationAction);
                    logger.info("Registration for email = {} not started yet!", login);
                    throw new RegistrationNotStartedException("Registration not started yet!");
                }

                verifySession(registrationAction, request.getSessionId());
                verifyState(ActionState.OTP_PASSED, registrationAction);

                getRepository().delete(registrationAction);

                ClientCreate clientCreate = new ClientCreate()
                        .setEmail(login)
                        .setPassword(request.getPassword())
                        .setFirstName(request.getFirstName())
                        .setLastName(request.getLastName())
                        .setAge(request.getAge());
                return clientService.create(clientCreate);
            });
        }

        protected RegistrationActionEntity createNewActionAndSendOtp(String login, String sessionId) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime actionExpiredAt = now.plusSeconds(otpActionLifetimeSeconds);

            OtpActionEntity otp = new OtpActionEntity()
                    .setStartedDate(now)
                    .setActionExpiredAt(actionExpiredAt)
                    .setOtpInvalidAttemptsLeft(optMaxInvalidAttempts)
                    .setOtpCountLeft(otpMaxNewGenerationPerAction);

            RegistrationActionEntity registrationAction = new RegistrationActionEntity()
                    .setLogin(login)
                    .setSessionId(sessionId)
                    .setOtpAction(otp);
            return updateNewOtpAndSendToClient(registrationAction);
        }

        @Override
        protected String getActionName() {
            return "registration-action";
        }

        @Override
        protected String getLock(String payload) {
            return DBLockPrefixesConstants.REG_PREFIX + payload;
        }

        @Override
        protected ActionRepository<RegistrationActionEntity> getRepository() {
            return registrationActionRepository;
        }

        @Override
        protected void sendNewOtpToClient(RegistrationActionEntity action) {
            String login = action.getLogin();
            notificationProvider.sendRegistrationCode(login, action.getOtpAction().getOtpValue());
        }

        @Override
        @SuppressWarnings("unchecked")
        protected RegistrationActionResult buildActionInfo(RegistrationActionEntity action) {
            return new RegistrationActionResult(action);
        }

        private void verifyState(ActionState expectedState, RegistrationActionEntity registrationActionEntity) {
            if (registrationActionEntity.getState() == expectedState) {
                return;
            }

            String errorMessage = String.format("Registration for login=[%s] must be in state=[%s]",
                    registrationActionEntity.getLogin(), expectedState);
            throw new RegistrationWrongStateException(errorMessage);
        }
}
