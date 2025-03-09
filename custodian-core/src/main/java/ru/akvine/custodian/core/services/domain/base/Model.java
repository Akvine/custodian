package ru.akvine.custodian.core.services.domain.base;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public abstract class Model {
    protected ZonedDateTime createdDate;
    @Nullable
    protected ZonedDateTime updatedDate;
}

