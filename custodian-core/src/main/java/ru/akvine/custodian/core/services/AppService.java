package ru.akvine.custodian.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.exceptions.app.AppNotFoundException;
import ru.akvine.custodian.core.exceptions.property.PropertyAlreadyExistsException;
import ru.akvine.custodian.core.repositories.AppRepository;
import ru.akvine.custodian.core.repositories.entities.AppEntity;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;
import ru.akvine.custodian.core.services.domain.AppBean;
import ru.akvine.custodian.core.services.dto.app.AppCreate;
import ru.akvine.custodian.core.utils.Asserts;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {
    private final AppRepository appRepository;
    private final ClientService clientService;

    public AppBean create(AppCreate appCreate) {
        Asserts.isNotNull(appCreate, "appCreate is null");
        logger.debug("App create by = {}", appCreate);

        ClientEntity client = clientService.verifyExistsByUuid(appCreate.getClientUuid());

        String title = appCreate.getTitle();
        long clientId = client.getId();
        Optional<AppEntity> appEntityOptional = appRepository.findByTitleAndClientId(title, clientId);
        if (appEntityOptional.isPresent()) {
            String errorMessage = String.format("App with title = [%s] already exists!", title);
            throw new PropertyAlreadyExistsException(errorMessage);
        }

        AppEntity app = new AppEntity()
                .setClient(client)
                .setTitle(appCreate.getTitle())
                .setDescription(appCreate.getDescription());

        AppBean savedApp = new AppBean(appRepository.save(app));
        logger.debug("Successful save app = {}", savedApp);
        return savedApp;
    }

    public List<AppBean> list(long clientId) {
        return listEntities(clientId)
                .stream()
                .map(AppBean::new)
                .toList();
    }

    public List<AppEntity> listEntities(long clientId) {
        logger.debug("List apps for client with id = {}", clientId);
        return appRepository.findByClientId(clientId);
    }

    public AppEntity verifyExistsByTitle(String title, long clientId) {
        Asserts.isNotNull(title, "title is null");
        return appRepository
                .findByTitleAndClientId(title, clientId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "App with title = [%s] for current user not found",
                            title);
                    return new AppNotFoundException(errorMessage);
                });
    }
}
