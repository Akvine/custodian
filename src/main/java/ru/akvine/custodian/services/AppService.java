package ru.akvine.custodian.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.exceptions.app.AppNotFoundException;
import ru.akvine.custodian.exceptions.property.PropertyAlreadyExistsException;
import ru.akvine.custodian.repositories.AppRepository;
import ru.akvine.custodian.repositories.entities.AppEntity;
import ru.akvine.custodian.repositories.entities.ClientEntity;
import ru.akvine.custodian.services.domain.AppBean;
import ru.akvine.custodian.services.dto.app.AppCreate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {
    private final AppRepository appRepository;
    private final ClientService clientService;

    public AppBean create(AppCreate appCreate) {
        Preconditions.checkNotNull(appCreate, "appCreate is null");
        logger.debug("App create by = {}", appCreate);

        String title = appCreate.getTitle();
        Optional<AppEntity> appEntityOptional = appRepository.findByTitle(title);
        if (appEntityOptional.isPresent()) {
            String errorMessage = String.format("App with title = [%s] already exists!", title);
            throw new PropertyAlreadyExistsException(errorMessage);
        }

        ClientEntity client = clientService.verifyExistsByUuid(appCreate.getClientUuid());
        AppEntity app = new AppEntity()
                .setClient(client)
                .setTitle(appCreate.getTitle())
                .setDescription(appCreate.getDescription());

        AppBean savedApp = new AppBean(appRepository.save(app));
        logger.debug("Successful save app = {}", savedApp);
        return savedApp;
    }

    public AppEntity verifyExistsByTitle(String title) {
        Preconditions.checkNotNull(title, "title is null");
        return appRepository
                .findByTitle(title)
                .orElseThrow(() -> new AppNotFoundException("App with title = [" + title + "] not found!"));
    }
}
