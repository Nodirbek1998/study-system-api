package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link uz.tatu.domain.Groups} entity.
 */
public class GroupsDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Set<StudyUsersDTO> studyUsers = new HashSet<>();

    private Set<SubjectsDTO> subjects = new HashSet<>();

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

    public Set<StudyUsersDTO> getStudyUsers() {
        return studyUsers;
    }

    public void setStudyUsers(Set<StudyUsersDTO> studyUsers) {
        this.studyUsers = studyUsers;
    }

    public Set<SubjectsDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectsDTO> subjects) {
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupsDTO)) {
            return false;
        }

        GroupsDTO groupsDTO = (GroupsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, groupsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", studyUsers=" + getStudyUsers() +
            ", subjects=" + getSubjects() +
            "}";
    }
}
