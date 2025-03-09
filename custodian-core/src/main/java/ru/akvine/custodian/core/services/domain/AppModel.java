package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.AppEntity;
import ru.akvine.custodian.core.services.domain.base.SoftModel;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class AppModel extends SoftModel {
    private Long id;
    private String title;
    @Nullable
    private String description;
    private ClientModel client;

    public AppModel(AppEntity app) {
        this.id = app.getId();
        this.title = app.getTitle();
        this.description = app.getDescription();
        this.client = new ClientModel(app.getClient());

        this.createdDate = client.getCreatedDate();
        this.updatedDate = client.getUpdatedDate();
        this.deletedDate = client.getDeletedDate();
        this.deleted = client.isDeleted();
    }
}
