package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.PropertyEntity;
import ru.akvine.custodian.core.services.domain.base.SoftModel;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class PropertyModel extends SoftModel {
    private Long id;
    private String profile;
    private String key;
    private String value;
    @Nullable
    private String description;
    private AppModel app;
    @Nullable
    private Character mask;
    @Nullable
    private Integer maskingRadius;

    public PropertyModel(PropertyEntity property) {
        this.id = property.getId();
        this.profile = property.getProfile();
        this.key = property.getKey();
        this.value = property.getValue();
        this.description = property.getDescription();
        this.app = new AppModel(property.getApp());
        this.mask = property.getMask();
        this.maskingRadius = property.getMaskingRadius();

        this.createdDate = property.getCreatedDate();
        this.updatedDate = property.getUpdatedDate();
        this.deletedDate = property.getDeletedDate();
        this.deleted = property.isDeleted();
    }
}
