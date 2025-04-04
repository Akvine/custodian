package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;
import ru.akvine.custodian.core.services.domain.base.SoftModel;

@Data
@Accessors(chain = true)
public class ClientModel extends SoftModel {
    private Long id;
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private int age;

    @Nullable
    @ToString.Exclude
    private String password;
    @Nullable
    @ToString.Exclude
    private String hash;

    public ClientModel(ClientEntity client) {
        this.id = client.getId();
        this.uuid = client.getUuid();
        this.email = client.getEmail();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.age = client.getAge();
        this.hash = client.getHash();

        this.createdDate = client.getCreatedDate();
        this.updatedDate = client.getUpdatedDate();
        this.deletedDate = client.getDeletedDate();
        this.deleted = client.isDeleted();
    }
}
