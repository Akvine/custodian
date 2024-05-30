package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;
import ru.akvine.custodian.core.services.domain.base.SoftBean;

@Data
@Accessors(chain = true)
public class ClientBean extends SoftBean {
    private Long id;
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;
    private int age;

    public ClientBean(ClientEntity client) {
        this.id = client.getId();
        this.uuid = client.getUuid();
        this.email = client.getEmail();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.age = client.getAge();

        this.createdDate = client.getCreatedDate();
        this.updatedDate = client.getUpdatedDate();
        this.deletedDate = client.getDeletedDate();
        this.deleted = client.isDeleted();
    }
}
