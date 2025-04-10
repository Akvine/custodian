package ru.akvine.custodian.core.services.domain.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public abstract class SoftModel extends Model {
    protected ZonedDateTime deletedDate;
    protected boolean deleted;
}
