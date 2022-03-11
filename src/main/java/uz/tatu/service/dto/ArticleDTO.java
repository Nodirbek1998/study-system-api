package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.tatu.domain.Article} entity.
 */
public class ArticleDTO implements Serializable {

    private Long id;

    private String name;

    @Size(max = 1000)
    private String text;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private StudyUsersDTO studyUser;

    private StudyUsersDTO createdBy;

    private StudyUsersDTO updatedBy;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public StudyUsersDTO getStudyUser() {
        return studyUser;
    }

    public void setStudyUser(StudyUsersDTO studyUser) {
        this.studyUser = studyUser;
    }

    public StudyUsersDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StudyUsersDTO createdBy) {
        this.createdBy = createdBy;
    }

    public StudyUsersDTO getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(StudyUsersDTO updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleDTO)) {
            return false;
        }

        ArticleDTO articleDTO = (ArticleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", text='" + getText() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", studyUser=" + getStudyUser() +
            ", createdBy=" + getCreatedBy() +
            ", updatedBy=" + getUpdatedBy() +
            "}";
    }
}
