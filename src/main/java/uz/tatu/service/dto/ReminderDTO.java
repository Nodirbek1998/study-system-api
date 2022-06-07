package uz.tatu.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * A DTO for the {@link uz.tatu.domain.Reminder} entity.
 */


@Data
@EqualsAndHashCode
@ToString
public class ReminderDTO {

    private Long id;

    private String title;

    private String body;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Long createdBy;


}
