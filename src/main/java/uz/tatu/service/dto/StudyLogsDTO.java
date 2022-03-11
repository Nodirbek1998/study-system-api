package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import uz.tatu.domain.enumeration.EnumActionType;

/**
 * A DTO for the {@link uz.tatu.domain.StudyLogs} entity.
 */
public class StudyLogsDTO implements Serializable {

    private Long id;

    private String operationName;

    private String clientIp;

    private String host;

    private LocalDate createdAt;

    private EnumActionType actionType;

    private StudyUsersDTO studyUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public EnumActionType getActionType() {
        return actionType;
    }

    public void setActionType(EnumActionType actionType) {
        this.actionType = actionType;
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
        if (!(o instanceof StudyLogsDTO)) {
            return false;
        }

        StudyLogsDTO studyLogsDTO = (StudyLogsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studyLogsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudyLogsDTO{" +
            "id=" + getId() +
            ", operationName='" + getOperationName() + "'" +
            ", clientIp='" + getClientIp() + "'" +
            ", host='" + getHost() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", actionType='" + getActionType() + "'" +
            ", studyUser=" + getStudyUser() +
            "}";
    }
}
