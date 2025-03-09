package ru.akvine.custodian.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.exceptions.app.AppNotFoundException;
import ru.akvine.custodian.core.exceptions.property.PropertyAlreadyExistsException;
import ru.akvine.custodian.core.repositories.AppRepository;
import ru.akvine.custodian.core.repositories.PropertyRepository;
import ru.akvine.custodian.core.repositories.entities.AppEntity;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;
import ru.akvine.custodian.core.services.domain.AppModel;
import ru.akvine.custodian.core.services.dto.app.AppCreate;
import ru.akvine.custodian.core.services.dto.app.AppDelete;
import ru.akvine.custodian.core.services.dto.app.AppUpdate;
import ru.akvine.custodian.core.utils.Asserts;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {
    private final PropertyRepository propertyRepository;
    private final AppRepository appRepository;
    private final ClientService clientService;

    public AppModel create(AppCreate appCreate) {
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

        AppModel savedApp = new AppModel(appRepository.save(app));
        logger.debug("Successful save app = {}", savedApp);
        return savedApp;
    }

    public AppModel update(AppUpdate appUpdate) {
        Asserts.isNotNull(appUpdate);

        AppEntity appToUpdate = verifyExistsByTitle(appUpdate.getAppTitle(), appUpdate.getClientId());
        if (StringUtils.isNotBlank(appUpdate.getNewTitle()) &&
                !appToUpdate.getTitle().equals(appUpdate.getNewTitle())) {
            appToUpdate.setTitle(appUpdate.getNewTitle());
        }

        if (StringUtils.isNotBlank(appUpdate.getNewDescription()) &&
                !appUpdate.getNewDescription().equals(appToUpdate.getDescription())) {
            appToUpdate.setDescription(appUpdate.getNewDescription());
        }

        return new AppModel(appRepository.save(appToUpdate));
    }

    public void delete(AppDelete appDelete) {
        Asserts.isNotNull(appDelete, "appDelete is null");

        String appTitle = appDelete.getAppTitle();
        AppEntity appToDelete = verifyExistsByTitle(appTitle, appDelete.getClientId());

        propertyRepository.delete(appTitle, ZonedDateTime.now());

        appToDelete.setDeleted(true);
        appToDelete.setDeletedDate(ZonedDateTime.now());

        appRepository.save(appToDelete);
    }

    public List<AppModel> list(long clientId) {
        return listEntities(clientId)
                .stream()
                .map(AppModel::new)
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
