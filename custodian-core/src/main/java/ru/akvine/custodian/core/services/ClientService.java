package ru.akvine.custodian.core.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.commons.util.UUIDGenerator;
import ru.akvine.custodian.core.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.custodian.core.exceptions.client.ClientNotFoundException;
import ru.akvine.custodian.core.repositories.ClientRepository;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;
import ru.akvine.custodian.core.services.domain.ClientBean;
import ru.akvine.custodian.core.services.dto.client.ClientCreate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientBean create(ClientCreate clientCreate) {
        Preconditions.checkNotNull(clientCreate, "clientCreate is null");
        logger.debug("Create client by = {}", clientCreate);

        String email = clientCreate.getEmail();
        Optional<ClientEntity> clientEntityOptional = clientRepository.findByEmail(email);
        if (clientEntityOptional.isPresent()) {
            throw new ClientAlreadyExistsException("Client with email = [" + email + "] already exists!");
        }

        ClientEntity clientEntity = new ClientEntity()
                .setUuid(UUIDGenerator.uuidWithNoDashes())
                .setFirstName(clientCreate.getFirstName())
                .setLastName(clientCreate.getLastName())
                .setEmail(clientCreate.getEmail())
                .setAge(clientCreate.getAge());

        ClientBean clientBean = new ClientBean(clientRepository.save(clientEntity));
        logger.debug("Successful save client = {}", clientBean);
        return clientBean;
    }

    public ClientEntity verifyExistsByUuid(String clientUuid) {
        Preconditions.checkNotNull(clientUuid, "clientUuid is null");
        return clientRepository
                .findByUuid(clientUuid)
                .orElseThrow(() -> new ClientNotFoundException("Client with uuid = [" + clientUuid + "] not found!"));
    }
}
