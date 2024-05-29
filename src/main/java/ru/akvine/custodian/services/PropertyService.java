package ru.akvine.custodian.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.custodian.controllers.rest.dto.property.PropertyImport;
import ru.akvine.custodian.exceptions.property.PropertyAlreadyExistsException;
import ru.akvine.custodian.repositories.PropertyRepository;
import ru.akvine.custodian.repositories.entities.AppEntity;
import ru.akvine.custodian.repositories.entities.PropertyEntity;
import ru.akvine.custodian.services.domain.PropertyBean;
import ru.akvine.custodian.services.dto.property.PropertyCreate;
import ru.akvine.custodian.services.dto.property.PropertyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final AppService appService;

    public PropertyBean create(PropertyCreate propertyCreate) {
        Preconditions.checkNotNull(propertyCreate, "propertyCreate is null");
        logger.debug("Create property by = [{}]", propertyCreate);

        String profile = propertyCreate.getProfile();
        String key = propertyCreate.getKey();
        String appTitle = propertyCreate.getAppTitle();
        Optional<PropertyEntity> propertyEntityOptional = propertyRepository.findByAppTitleAndProfileAndKey(
                appTitle,
                profile,
                key);
        if (propertyEntityOptional.isPresent()) {
            String errorMessage = String.format(
                    "Property with profile = [%s] and key = [%s] already exists!",
                    profile,
                    key);
            throw new PropertyAlreadyExistsException(errorMessage);
        }

        AppEntity app = appService.verifyExistsByTitle(propertyCreate.getAppTitle());
        PropertyEntity propertyEntity = new PropertyEntity()
                .setProfile(profile)
                .setKey(key)
                .setValue(propertyCreate.getValue())
                .setApp(app);
        PropertyBean savedProperty = new PropertyBean(propertyRepository.save(propertyEntity));
        logger.debug("Successful create property = {}", savedProperty);
        return savedProperty;
    }

    public List<PropertyBean> list(PropertyList propertyList) {
        Preconditions.checkNotNull(propertyList, "propertyList is null");
        logger.debug("List properties by = {}", propertyList);

        String appTitle = propertyList.getAppTitle();
        List<PropertyBean> properties;
        if (CollectionUtils.isEmpty(propertyList.getProfiles()) &&
                CollectionUtils.isEmpty(propertyList.getProfilesAndKeys())) {
            properties = propertyRepository
                    .findByAppTitle(appTitle)
                    .stream()
                    .map(PropertyBean::new)
                    .toList();
        } else if (CollectionUtils.isEmpty(propertyList.getProfilesAndKeys())) {
            properties = propertyRepository
                    .findByAppTitleAndProfiles(appTitle, propertyList.getProfiles())
                    .stream()
                    .map(PropertyBean::new)
                    .toList();
        } else {
            properties = new ArrayList<>();
            propertyList.getProfilesAndKeys().forEach((profile, keys) -> properties.addAll(
                    propertyRepository
                            .findByAppTitleAndProfileAndKeys(appTitle, profile, keys)
                            .stream()
                            .map(PropertyBean::new)
                            .toList()));
        }

        return properties;
    }

    public boolean importProperties(PropertyImport propertyImport) {
        Preconditions.checkNotNull(propertyImport, "propertyImport is null");

        String appTitle = propertyImport.getAppTitle();
        String profile = propertyImport.getProfile();
        logger.debug("Import properties for appTitle = {} with profile = {}", appTitle, profile);

        AppEntity app = appService.verifyExistsByTitle(appTitle);
        propertyImport.getProperties().forEach((key, value) -> {
            PropertyEntity propertyEntity = new PropertyEntity()
                    .setApp(app)
                    .setProfile(profile)
                    .setKey(key)
                    .setValue(value);
            propertyRepository.save(propertyEntity);
        });

        logger.debug("Successful import all properties");
        return true;
    }
}
