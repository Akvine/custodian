package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.PropertyEntity;
import ru.akvine.custodian.core.services.domain.base.SoftBean;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class PropertyBean extends SoftBean {
    private Long id;
    private String profile;
    private String key;
    private String value;
    @Nullable
    private String description;
    private AppBean app;

    public PropertyBean(PropertyEntity property) {
        this.id = property.getId();
        this.profile = property.getProfile();
        this.key = property.getKey();
        this.value = property.getValue();
        this.description = property.getDescription();
        this.app = new AppBean(property.getApp());

        this.createdDate = property.getCreatedDate();
        this.updatedDate = property.getUpdatedDate();
        this.deletedDate = property.getDeletedDate();
        this.deleted = property.isDeleted();
    }
}
