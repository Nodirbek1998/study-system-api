package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.enumeration.EnumActionType;

/**
 * A StudyLogs.
 */
@Entity
@Table(name = "study_logs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudyLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "operation_name")
    private String operationName;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "host")
    private String host;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private EnumActionType actionType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private StudyUsers studyUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudyLogs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public StudyLogs operationName(String operationName) {
        this.setOperationName(operationName);
        return this;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getClientIp() {
        return this.clientIp;
    }

    public StudyLogs clientIp(String clientIp) {
        this.setClientIp(clientIp);
        return this;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getHost() {
        return this.host;
    }

    public StudyLogs host(String host) {
        this.setHost(host);
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public StudyLogs createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public EnumActionType getActionType() {
        return this.actionType;
    }

    public StudyLogs actionType(EnumActionType actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(EnumActionType actionType) {
        this.actionType = actionType;
    }

    public StudyUsers getStudyUser() {
        return this.studyUser;
    }

    public void setStudyUser(StudyUsers studyUsers) {
        this.studyUser = studyUsers;
    }

    public StudyLogs studyUser(StudyUsers studyUsers) {
        this.setStudyUser(studyUsers);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudyLogs)) {
            return false;
        }
        return id != null && id.equals(((StudyLogs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudyLogs{" +
            "id=" + getId() +
            ", operationName='" + getOperationName() + "'" +
            ", clientIp='" + getClientIp() + "'" +
            ", host='" + getHost() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", actionType='" + getActionType() + "'" +
            "}";
    }
}
