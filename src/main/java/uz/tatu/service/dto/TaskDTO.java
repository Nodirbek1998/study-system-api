package uz.tatu.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.service.utils.CardLocaleDateDeserializer;
import uz.tatu.service.utils.CardLocaleTimeDeserializer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @JsonDeserialize(using = CardLocaleDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", timezone = "Asia/Tashkent")
    private LocalDate deadline;

    @JsonDeserialize(using = CardLocaleTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Tashkent")
    private LocalTime time;


    private FilesDTO filesDTO;

    private Long fileId;

    private Long unitsId;

    private Double ball;


}
