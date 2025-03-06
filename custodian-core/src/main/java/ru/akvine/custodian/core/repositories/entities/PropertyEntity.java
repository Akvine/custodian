package ru.akvine.custodian.core.repositories.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.base.SoftBaseEntity;

@Entity
@Table(name = "PROPERTY_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class PropertyEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propertyEntitySeq")
    @SequenceGenerator(name = "propertyEntitySeq", sequenceName = "SEQ_PROPERTY_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "PROFILE", nullable = false)
    private String profile;

    @Column(name = "KEY", nullable = false)
    private String key;

    @Column(name = "VALUE", nullable = false)
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "APP_ID", nullable = false)
    private AppEntity app;

    @Column(name = "MASK")
    @Nullable
    private Character mask;

    @Column(name = "MASKING_RADIUS")
    @Nullable
    private Integer maskingRadius;
}
