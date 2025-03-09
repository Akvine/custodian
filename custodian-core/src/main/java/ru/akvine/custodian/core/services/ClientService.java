package ru.akvine.custodian.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.commons.util.UUIDGenerator;
import ru.akvine.custodian.core.exceptions.client.ClientNotFoundException;
import ru.akvine.custodian.core.repositories.ClientRepository;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;
import ru.akvine.custodian.core.services.domain.ClientModel;
import ru.akvine.custodian.core.services.dto.client.ClientCreate;
import ru.akvine.custodian.core.services.security.PasswordService;
import ru.akvine.custodian.core.utils.Asserts;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordService passwordService;

    public ClientModel create(ClientCreate clientCreate) {
        Asserts.isNotNull(clientCreate, "clientCreate is null");
        logger.debug("Create client by = {}", clientCreate);

        String passwordHash = passwordService.encodePassword(clientCreate.getPassword());
        ClientEntity clientEntity = new ClientEntity()
                .setUuid(UUIDGenerator.uuidWithNoDashes())
                .setFirstName(clientCreate.getFirstName())
                .setLastName(clientCreate.getLastName())
                .setEmail(clientCreate.getEmail())
                .setAge(clientCreate.getAge())
                .setHash(passwordHash);

        ClientModel clientBean = new ClientModel(clientRepository.save(clientEntity));
        logger.debug("Successful save client = {}", clientBean);
        return clientBean;
    }

    public ClientModel updatePassword(String login, String newHash) {
        logger.info("Update password for client with email = {}", login);

        ClientEntity client = verifyExistsByEmail(login);
        client.setHash(newHash);
        client.setUpdatedDate(ZonedDateTime.now());

        logger.info("Successful update password for client with email = {}", login);
        return new ClientModel(clientRepository.save(client));
    }

    public ClientModel getByEmail(String email) {
        Asserts.isNotNull(email, "email is null");
        logger.info("Try to get client with email = {}", email);
        return new ClientModel(verifyExistsByEmail(email));
    }

    public ClientEntity verifyExistsByUuid(String clientUuid) {
        Asserts.isNotNull(clientUuid, "clientUuid is null");
        logger.info("Check client exists with uuid and get = {}", clientUuid);
        return clientRepository
                .findByUuid(clientUuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + clientUuid + "] not found!"));
    }

    public ClientEntity verifyExistsByEmail(String email) {
        Asserts.isNotNull(email, "email is null");
        logger.info("Check client exists with email and get = {}", email);
        return clientRepository
                .findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client with email = [" + email + "] not found!"));
    }

    public boolean isExistsByEmail(String email) {
        Asserts.isNotNull(email, "email is null");
        logger.info("Check client exists with email = {}", email);
        return clientRepository.findByEmail(email).isPresent();
    }
}
