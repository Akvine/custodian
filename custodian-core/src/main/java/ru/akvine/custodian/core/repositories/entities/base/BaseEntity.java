package ru.akvine.custodian.core.repositories.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseEntity {
    @Column(name = "CREATED_DATE", nullable = false)
    private final ZonedDateTime createdDate = ZonedDateTime.now();

    @Column(name = "UPDATED_DATE")
    private ZonedDateTime updatedDate;
}
