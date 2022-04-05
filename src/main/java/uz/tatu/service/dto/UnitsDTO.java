package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Units} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class UnitsDTO implements Serializable {

    private Long id;

    private String nameUz;

    private String nameRu;

    private String nameEn;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Long subjectId;

}
