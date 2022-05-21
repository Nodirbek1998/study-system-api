package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.audit.DateAudit;
import uz.tatu.domain.enumeration.EnumTest;

/**
 * A Tests.
 */
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "tests")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tests extends DateAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumTest status;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToOne
    @JsonIgnoreProperties(value = { "groups" }, allowSetters = true)
    private Subjects subject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

}
