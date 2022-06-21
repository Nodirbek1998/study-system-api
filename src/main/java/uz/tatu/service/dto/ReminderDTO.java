package uz.tatu.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.service.utils.CardLocaleDateDeserializer;

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

    @JsonDeserialize(using = CardLocaleDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Tashkent")
    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Long createdById;


}
