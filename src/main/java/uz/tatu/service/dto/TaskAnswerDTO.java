package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link uz.tatu.domain.TaskAnswer} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class TaskAnswerDTO implements Serializable {

    private Long id;

    private LocalDate createdAt;

    private LocalDate updatedAt;


}
