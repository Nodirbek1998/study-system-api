package uz.tatu.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.domain.enumeration.EnumTest;

/**
 * A DTO for the {@link uz.tatu.domain.Tests} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class TestsDTO implements Serializable {

    private Long id;

    private String name;

    private EnumTest status;

    private LocalDate deadline;

    private Long subjectId;

}
