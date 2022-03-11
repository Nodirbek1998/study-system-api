package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Files} entity.
 */
public class FilesDTO implements Serializable {

    private Long id;

    private String name;

    private Double fileSize;

    private String contentType;

    private LocalDate createdAt;

    private StudyUsersDTO createdBy;

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

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public StudyUsersDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StudyUsersDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilesDTO)) {
            return false;
        }

        FilesDTO filesDTO = (FilesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fileSize=" + getFileSize() +
            ", contentType='" + getContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
