package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Files} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class FilesDTO implements Serializable {

    private Long id;

    private String name;

    private Long fileSize;

    private String contentType;

    private LocalDate createdAt;

    private Long createdById;

}
