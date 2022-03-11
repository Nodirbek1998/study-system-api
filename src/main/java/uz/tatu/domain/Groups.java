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
 * A Groups.
 */
@Entity
@Table(name = "groups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToMany
    @JoinTable(
        name = "rel_groups__study_user",
        joinColumns = @JoinColumn(name = "groups_id"),
        inverseJoinColumns = @JoinColumn(name = "study_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private Set<StudyUsers> studyUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_groups__subjects",
        joinColumns = @JoinColumn(name = "groups_id"),
        inverseJoinColumns = @JoinColumn(name = "subjects_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "groups" }, allowSetters = true)
    private Set<Subjects> subjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Groups id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Groups name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Groups createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public Groups updatedAt(LocalDate updatedAt) {
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

    public Groups studyUsers(Set<StudyUsers> studyUsers) {
        this.setStudyUsers(studyUsers);
        return this;
    }

    public Groups addStudyUser(StudyUsers studyUsers) {
        this.studyUsers.add(studyUsers);
        studyUsers.getGroups().add(this);
        return this;
    }

    public Groups removeStudyUser(StudyUsers studyUsers) {
        this.studyUsers.remove(studyUsers);
        studyUsers.getGroups().remove(this);
        return this;
    }

    public Set<Subjects> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(Set<Subjects> subjects) {
        this.subjects = subjects;
    }

    public Groups subjects(Set<Subjects> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Groups addSubjects(Subjects subjects) {
        this.subjects.add(subjects);
        subjects.getGroups().add(this);
        return this;
    }

    public Groups removeSubjects(Subjects subjects) {
        this.subjects.remove(subjects);
        subjects.getGroups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Groups)) {
            return false;
        }
        return id != null && id.equals(((Groups) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Groups{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
