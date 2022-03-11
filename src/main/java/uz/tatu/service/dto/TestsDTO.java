package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import uz.tatu.domain.enumeration.EnumTest;

/**
 * A DTO for the {@link uz.tatu.domain.Tests} entity.
 */
public class TestsDTO implements Serializable {

    private Long id;

    private String name;

    private EnumTest status;

    private LocalDate deadline;

    private SubjectsDTO subject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumTest getStatus() {
        return status;
    }

    public void setStatus(EnumTest status) {
        this.status = status;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public SubjectsDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectsDTO subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestsDTO)) {
            return false;
        }

        TestsDTO testsDTO = (TestsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", subject=" + getSubject() +
            "}";
    }
}
