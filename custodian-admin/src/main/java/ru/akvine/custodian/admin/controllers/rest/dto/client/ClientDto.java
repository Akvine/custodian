package ru.akvine.custodian.admin.controllers.rest.dto.client;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDto {
    private String uuid;
    private String firstName;
    private String lastName;
    private int age;
    @ToString.Exclude
    private String email;
}
