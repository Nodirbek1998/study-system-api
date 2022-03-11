package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Images.
 */
@Entity
@Table(name = "images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_size")
    private Double imageSize;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private StudyUsers studyUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Images id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Images name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getImageSize() {
        return this.imageSize;
    }

    public Images imageSize(Double imageSize) {
        this.setImageSize(imageSize);
        return this;
    }

    public void setImageSize(Double imageSize) {
        this.imageSize = imageSize;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Images contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Images createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public StudyUsers getStudyUser() {
        return this.studyUser;
    }

    public void setStudyUser(StudyUsers studyUsers) {
        this.studyUser = studyUsers;
    }

    public Images studyUser(StudyUsers studyUsers) {
        this.setStudyUser(studyUsers);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Images)) {
            return false;
        }
        return id != null && id.equals(((Images) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Images{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imageSize=" + getImageSize() +
            ", contentType='" + getContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
