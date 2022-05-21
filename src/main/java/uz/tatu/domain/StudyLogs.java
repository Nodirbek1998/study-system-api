package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.audit.DateAudit;
import uz.tatu.domain.enumeration.EnumActionType;

/**
 * A StudyLogs.
 */
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "study_logs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudyLogs extends DateAudit implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private EnumActionType actionType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "role", "groups", "testAnswers", "taskAnswers" }, allowSetters = true)
    private User users;


}
