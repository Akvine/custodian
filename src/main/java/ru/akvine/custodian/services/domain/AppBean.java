package ru.akvine.custodian.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.repositories.entities.AppEntity;
import ru.akvine.custodian.services.domain.base.SoftBean;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class AppBean extends SoftBean {
    private Long id;
    private String title;
    @Nullable
    private String description;
    private ClientBean client;

    public AppBean(AppEntity app) {
        this.id = app.getId();
        this.title = app.getTitle();
        this.description = app.getDescription();
        this.client = new ClientBean(app.getClient());

        this.createdDate = client.getCreatedDate();
        this.updatedDate = client.getUpdatedDate();
        this.deletedDate = client.getDeletedDate();
        this.deleted = client.isDeleted();
    }
}
