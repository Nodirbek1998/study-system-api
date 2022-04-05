package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Task} entity.
 */


@Data
@EqualsAndHashCode
@ToString
public class TaskDTO implements Serializable {

    private Long id;

    private String topic;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate deadline;

}
