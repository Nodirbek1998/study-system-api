package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link uz.tatu.domain.TestAnswer} entity.
 */
public class TestAnswerDTO implements Serializable {

    private Long id;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Boolean right;

    private Set<StudyUsersDTO> studyUsers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    public Set<StudyUsersDTO> getStudyUsers() {
        return studyUsers;
    }

    public void setStudyUsers(Set<StudyUsersDTO> studyUsers) {
        this.studyUsers = studyUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestAnswerDTO)) {
            return false;
        }

        TestAnswerDTO testAnswerDTO = (TestAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestAnswerDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", right='" + getRight() + "'" +
            ", studyUsers=" + getStudyUsers() +
            "}";
    }
}
