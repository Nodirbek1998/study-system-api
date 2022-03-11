package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Units} entity.
 */
public class UnitsDTO implements Serializable {

    private Long id;

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private SubjectsDTO subject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
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
        if (!(o instanceof UnitsDTO)) {
            return false;
        }

        UnitsDTO unitsDTO = (UnitsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, unitsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UnitsDTO{" +
            "id=" + getId() +
            ", nameUz='" + getNameUz() + "'" +
            ", nameRu='" + getNameRu() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", subject=" + getSubject() +
            "}";
    }
}
