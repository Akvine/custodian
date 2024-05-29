package ru.akvine.custodian.repositories.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.custodian.repositories.entities.base.SoftBaseEntity;

@Entity
@Table(name = "APP_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class AppEntity extends SoftBaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appEntitySeq")
    @SequenceGenerator(name = "appEntitySeq", sequenceName = "SEQ_APP_ENTITY", allocationSize = 1000)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientEntity client;
}
