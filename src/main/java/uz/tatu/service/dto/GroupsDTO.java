package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link uz.tatu.domain.Groups} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class GroupsDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate createdAt;

    private LocalDate updatedAt;


}
