package ru.akvine.custodian.services.dto.client;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientCreate {
    @ToString.Exclude
    private String email;
    private String firstName;
    private String lastName;
    private int age;
}
