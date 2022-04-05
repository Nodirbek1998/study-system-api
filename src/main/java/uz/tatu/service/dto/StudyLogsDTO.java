package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.domain.enumeration.EnumActionType;

/**
 * A DTO for the {@link uz.tatu.domain.StudyLogs} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class StudyLogsDTO implements Serializable {

    private Long id;

    private String operationName;

    private String clientIp;

    private String host;

    private LocalDate createdAt;

    private EnumActionType actionType;

    private Long userId;
}
