package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaskAnswer.
 */
@Entity
@Table(name = "task_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToMany
    @JoinTable(
        name = "rel_task_answer__study_user",
        joinColumns = @JoinColumn(name = "task_answer_id"),
        inverseJoinColumns = @JoinColumn(name = "study_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private Set<StudyUsers> studyUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaskAnswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public TaskAnswer createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public TaskAnswer updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<StudyUsers> getStudyUsers() {
        return this.studyUsers;
    }

    public void setStudyUsers(Set<StudyUsers> studyUsers) {
        this.studyUsers = studyUsers;
    }

    public TaskAnswer studyUsers(Set<StudyUsers> studyUsers) {
        this.setStudyUsers(studyUsers);
        return this;
    }

    public TaskAnswer addStudyUser(StudyUsers studyUsers) {
        this.studyUsers.add(studyUsers);
        studyUsers.getTaskAnswers().add(this);
        return this;
    }

    public TaskAnswer removeStudyUser(StudyUsers studyUsers) {
        this.studyUsers.remove(studyUsers);
        studyUsers.getTaskAnswers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskAnswer)) {
            return false;
        }
        return id != null && id.equals(((TaskAnswer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskAnswer{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
