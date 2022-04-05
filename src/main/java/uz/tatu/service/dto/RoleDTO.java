package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Role} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class RoleDTO implements Serializable {

    private Long id;

    private String nameUz;

    private String nameRu;

    private String nameEn;

}
