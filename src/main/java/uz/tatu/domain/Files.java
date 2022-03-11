package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Files.
 */
@Entity
@Table(name = "files")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_size")
    private Double fileSize;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private StudyUsers createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Files id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Files name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFileSize() {
        return this.fileSize;
    }

    public Files fileSize(Double fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Files contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Files createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public StudyUsers getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(StudyUsers studyUsers) {
        this.createdBy = studyUsers;
    }

    public Files createdBy(StudyUsers studyUsers) {
        this.setCreatedBy(studyUsers);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Files)) {
            return false;
        }
        return id != null && id.equals(((Files) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Files{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fileSize=" + getFileSize() +
            ", contentType='" + getContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
