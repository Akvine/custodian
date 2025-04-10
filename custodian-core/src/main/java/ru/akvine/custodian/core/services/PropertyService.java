package ru.akvine.custodian.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.custodian.core.enums.ExportFileType;
import ru.akvine.custodian.core.exceptions.property.PropertyAlreadyExistsException;
import ru.akvine.custodian.core.exceptions.property.PropertyNotFoundException;
import ru.akvine.custodian.core.exceptions.property.PropertyUpdateException;
import ru.akvine.custodian.core.managers.ExportersManagers;
import ru.akvine.custodian.core.repositories.PropertyRepository;
import ru.akvine.custodian.core.repositories.entities.AppEntity;
import ru.akvine.custodian.core.repositories.entities.PropertyEntity;
import ru.akvine.custodian.core.services.domain.PropertyModel;
import ru.akvine.custodian.core.services.dto.property.*;
import ru.akvine.custodian.core.utils.Asserts;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {
    private final PropertyRepository propertyRepository;

    private final AppService appService;
    private final ExportersManagers exportersManagers;

    public PropertyModel create(PropertyCreate propertyCreate) {
        Asserts.isNotNull(propertyCreate, "propertyCreate is null");
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

        AppEntity app = appService.verifyExistsByTitle(
                propertyCreate.getAppTitle(),
                propertyCreate.getClientId());
        PropertyEntity propertyEntity = new PropertyEntity()
                .setProfile(profile)
                .setKey(key)
                .setValue(propertyCreate.getValue())
                .setApp(app)
                .setMask(propertyCreate.getMask())
                .setMaskingRadius(propertyCreate.getMaskingRadius());
        PropertyModel savedProperty = new PropertyModel(propertyRepository.save(propertyEntity));
        logger.debug("Successful create property = {}", savedProperty);
        return savedProperty;
    }

    public List<PropertyModel> list(PropertyList propertyList) {
        Asserts.isNotNull(propertyList, "propertyList is null");
        logger.debug("List properties by = {}", propertyList);

        String appTitle = propertyList.getAppTitle();
        appService.verifyExistsByTitle(appTitle, propertyList.getClientId());
        List<PropertyModel> properties;
        if (CollectionUtils.isEmpty(propertyList.getProfiles()) &&
                CollectionUtils.isEmpty(propertyList.getProfilesAndKeys())) {
            properties = propertyRepository
                    .findByAppTitle(appTitle)
                    .stream()
                    .map(PropertyModel::new)
                    .toList();
        } else if (CollectionUtils.isEmpty(propertyList.getProfilesAndKeys())) {
            properties = propertyRepository
                    .findByAppTitleAndProfiles(appTitle, propertyList.getProfiles())
                    .stream()
                    .map(PropertyModel::new)
                    .toList();
        } else {
            properties = new ArrayList<>();
            propertyList.getProfilesAndKeys().forEach((profile, keys) -> properties.addAll(
                    propertyRepository
                            .findByAppTitleAndProfileAndKeys(appTitle, profile, keys)
                            .stream()
                            .map(PropertyModel::new)
                            .toList()));
        }

        return properties;
    }

    public PropertyModel updateProperty(PropertyUpdate propertyUpdate) {
        Asserts.isNotNull(propertyUpdate);

        String appTitle = propertyUpdate.getAppTitle();
        String profile = propertyUpdate.getProfile();
        String key = propertyUpdate.getKey();

        appService.verifyExistsByTitle(propertyUpdate.getAppTitle(), propertyUpdate.getClientId());
        PropertyEntity propertyToUpdate = verifyExists(
                appTitle,
                profile,
                key
        );

        if (StringUtils.isNotBlank(propertyUpdate.getNewKey()) &&
                !propertyUpdate.getNewKey().equals(propertyToUpdate.getKey())) {
            propertyToUpdate.setKey(propertyUpdate.getKey());
        }

        if (StringUtils.isNotBlank(propertyUpdate.getNewProfile()) &&
                !propertyUpdate.getNewProfile().equals(propertyToUpdate.getProfile())) {
            propertyToUpdate.setProfile(propertyUpdate.getNewProfile());
        }

        if (StringUtils.isNotBlank(propertyUpdate.getNewValue()) &&
                !propertyUpdate.getNewValue().equals(propertyToUpdate.getValue())) {
            propertyToUpdate.setProfile(propertyUpdate.getNewValue());
        }

        if (StringUtils.isNotBlank(propertyUpdate.getNewDescription()) &&
                !propertyUpdate.getNewDescription().equals(propertyToUpdate.getDescription())) {
            propertyToUpdate.setDescription(propertyUpdate.getNewDescription());
        }

        if (propertyUpdate.getNewMask() != null &&
                !propertyUpdate.getNewMask().equals(propertyToUpdate.getMask())) {

            if (propertyToUpdate.getMaskingRadius() == null &&
                    propertyUpdate.getNewRadiusMask() == null) {
                String errorMessage = String.format(
                        "Can't update mask for property with app title = [%s] with profile = [%s] and key = [%s]. " +
                                "Mask in target property is blank",
                        appTitle, profile, key
                );
                throw new PropertyUpdateException(errorMessage);
            }

            propertyToUpdate.setMask(propertyUpdate.getNewMask());
        }

        if (propertyUpdate.getNewRadiusMask() != null &&
                propertyUpdate.getNewRadiusMask() >= 0 &&
                !propertyUpdate.getNewRadiusMask().equals(propertyToUpdate.getMaskingRadius())) {
            if (propertyToUpdate.getMaskingRadius() == null &&
                    propertyUpdate.getNewMask() == null) {
                String errorMessage = String.format(
                        "Can't update mask for property with app title = [%s] with profile = [%s] and key = [%s]. " +
                                "Masking radius in target property is blank",
                        appTitle, profile, key
                );
                throw new PropertyUpdateException(errorMessage);
            }

            propertyToUpdate.setMaskingRadius(propertyUpdate.getNewRadiusMask());
        }

        return new PropertyModel(propertyRepository.save(propertyToUpdate));
    }

    public boolean importProperties(PropertyImport propertyImport) {
        Asserts.isNotNull(propertyImport, "propertyImport is null");

        String appTitle = propertyImport.getAppTitle();
        String profile = propertyImport.getProfile();
        logger.debug("Import properties for appTitle = {} with profile = {}", appTitle, profile);

        AppEntity app = appService.verifyExistsByTitle(appTitle, propertyImport.getClientId());
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

    public byte[] exportProperties(PropertyExport propertyExport) {
        Asserts.isNotNull(propertyExport);

        String appTitle = propertyExport.getAppTitle();
        ExportFileType fileType = propertyExport.getFileType();

        Set<String> uniqueProfiles;
        if (CollectionUtils.isEmpty(propertyExport.getProfiles())) {
            uniqueProfiles = propertyRepository.findUniqueProfiles(propertyExport.getAppTitle());
        } else {
            uniqueProfiles = propertyExport.getProfiles();
        }

        Map<String, List<PropertyModel>> profilesWithProperties = new HashMap<>();
        for (String profile : uniqueProfiles) {
            List<PropertyModel> properties = propertyRepository
                    .findByAppTitleAndProfile(appTitle, profile)
                    .stream()
                    .map(PropertyModel::new)
                    .toList();
            profilesWithProperties.put(profile, properties);
        }

        return exportersManagers.getByType(fileType).export(profilesWithProperties);
    }

    public void delete(PropertyDelete propertyDelete) {
        Asserts.isNotNull(propertyDelete);

        String appTitle = propertyDelete.getAppTitle();
        appService.verifyExistsByTitle(appTitle, propertyDelete.getClientId());

        if (CollectionUtils.isEmpty(propertyDelete.getProfilesWithKeys())) {
            propertyRepository.delete(appTitle, ZonedDateTime.now());
        } else {
            Map<String, Set<String>> profilesWithProperties = propertyDelete.getProfilesWithKeys();
            for (String profile : profilesWithProperties.keySet()) {
                propertyRepository.delete(appTitle, profile, profilesWithProperties.get(profile), ZonedDateTime.now());
            }
        }

    }

    public PropertyEntity verifyExists(String byAppTitle, String byProfile, String byKey) {
        Asserts.isNotNull(byAppTitle, "byAppTitle is null");
        Asserts.isNotNull(byProfile, "byProfile is null");
        Asserts.isNotNull(byKey, "byKey is null");

        return propertyRepository
                .findByAppTitleAndProfileAndKey(byAppTitle, byProfile, byKey)
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "Property with app title = [%s], profile = [%s] and key = [%s] not found!",
                            byAppTitle, byProfile, byKey
                    );
                    return new PropertyNotFoundException(errorMessage);
                });
    }
}
