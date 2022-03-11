package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Images} entity.
 */
public class ImagesDTO implements Serializable {

    private Long id;

    private String name;

    private Double imageSize;

    private String contentType;

    private LocalDate createdAt;

    private StudyUsersDTO studyUser;

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

    public Double getImageSize() {
        return imageSize;
    }

    public void setImageSize(Double imageSize) {
        this.imageSize = imageSize;
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

    public StudyUsersDTO getStudyUser() {
        return studyUser;
    }

    public void setStudyUser(StudyUsersDTO studyUser) {
        this.studyUser = studyUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagesDTO)) {
            return false;
        }

        ImagesDTO imagesDTO = (ImagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imageSize=" + getImageSize() +
            ", contentType='" + getContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", studyUser=" + getStudyUser() +
            "}";
    }
}
