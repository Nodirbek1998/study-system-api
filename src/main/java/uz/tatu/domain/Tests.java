package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.enumeration.EnumTest;

/**
 * A Tests.
 */
@Entity
@Table(name = "tests")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tests implements Serializable {

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
    private LocalDate deadline;

    @ManyToOne
    @JsonIgnoreProperties(value = { "groups" }, allowSetters = true)
    private Subjects subject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tests id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tests name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumTest getStatus() {
        return this.status;
    }

    public Tests status(EnumTest status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(EnumTest status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public Tests deadline(LocalDate deadline) {
        this.setDeadline(deadline);
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Subjects getSubject() {
        return this.subject;
    }

    public void setSubject(Subjects subjects) {
        this.subject = subjects;
    }

    public Tests subject(Subjects subjects) {
        this.setSubject(subjects);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tests)) {
            return false;
        }
        return id != null && id.equals(((Tests) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tests{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", deadline='" + getDeadline() + "'" +
            "}";
    }
}
