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
 * A StudyUsers.
 */
@Entity
@Table(name = "study_users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudyUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    private Role role;

    @ManyToMany(mappedBy = "studyUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studyUsers", "subjects" }, allowSetters = true)
    private Set<Groups> groups = new HashSet<>();

    @ManyToMany(mappedBy = "studyUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studyUsers" }, allowSetters = true)
    private Set<TestAnswer> testAnswers = new HashSet<>();

    @ManyToMany(mappedBy = "studyUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "studyUsers" }, allowSetters = true)
    private Set<TaskAnswer> taskAnswers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudyUsers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public StudyUsers fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return this.age;
    }

    public StudyUsers age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return this.phone;
    }

    public StudyUsers phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public StudyUsers email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public StudyUsers username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public StudyUsers password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public StudyUsers createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public StudyUsers updatedAt(LocalDate updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public StudyUsers role(Role role) {
        this.setRole(role);
        return this;
    }

    public Set<Groups> getGroups() {
        return this.groups;
    }

    public void setGroups(Set<Groups> groups) {
        if (this.groups != null) {
            this.groups.forEach(i -> i.removeStudyUser(this));
        }
        if (groups != null) {
            groups.forEach(i -> i.addStudyUser(this));
        }
        this.groups = groups;
    }

    public StudyUsers groups(Set<Groups> groups) {
        this.setGroups(groups);
        return this;
    }

    public StudyUsers addGroup(Groups groups) {
        this.groups.add(groups);
        groups.getStudyUsers().add(this);
        return this;
    }

    public StudyUsers removeGroup(Groups groups) {
        this.groups.remove(groups);
        groups.getStudyUsers().remove(this);
        return this;
    }

    public Set<TestAnswer> getTestAnswers() {
        return this.testAnswers;
    }

    public void setTestAnswers(Set<TestAnswer> testAnswers) {
        if (this.testAnswers != null) {
            this.testAnswers.forEach(i -> i.removeStudyUser(this));
        }
        if (testAnswers != null) {
            testAnswers.forEach(i -> i.addStudyUser(this));
        }
        this.testAnswers = testAnswers;
    }

    public StudyUsers testAnswers(Set<TestAnswer> testAnswers) {
        this.setTestAnswers(testAnswers);
        return this;
    }

    public StudyUsers addTestAnswer(TestAnswer testAnswer) {
        this.testAnswers.add(testAnswer);
        testAnswer.getStudyUsers().add(this);
        return this;
    }

    public StudyUsers removeTestAnswer(TestAnswer testAnswer) {
        this.testAnswers.remove(testAnswer);
        testAnswer.getStudyUsers().remove(this);
        return this;
    }

    public Set<TaskAnswer> getTaskAnswers() {
        return this.taskAnswers;
    }

    public void setTaskAnswers(Set<TaskAnswer> taskAnswers) {
        if (this.taskAnswers != null) {
            this.taskAnswers.forEach(i -> i.removeStudyUser(this));
        }
        if (taskAnswers != null) {
            taskAnswers.forEach(i -> i.addStudyUser(this));
        }
        this.taskAnswers = taskAnswers;
    }

    public StudyUsers taskAnswers(Set<TaskAnswer> taskAnswers) {
        this.setTaskAnswers(taskAnswers);
        return this;
    }

    public StudyUsers addTaskAnswer(TaskAnswer taskAnswer) {
        this.taskAnswers.add(taskAnswer);
        taskAnswer.getStudyUsers().add(this);
        return this;
    }

    public StudyUsers removeTaskAnswer(TaskAnswer taskAnswer) {
        this.taskAnswers.remove(taskAnswer);
        taskAnswer.getStudyUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudyUsers)) {
            return false;
        }
        return id != null && id.equals(((StudyUsers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudyUsers{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", age=" + getAge() +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
