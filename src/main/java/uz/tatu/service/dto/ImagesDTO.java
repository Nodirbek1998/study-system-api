package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.Images} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class ImagesDTO implements Serializable {

    private Long id;

    private String name;

    private Double imageSize;

    private String contentType;

    private LocalDate createdAt;

    private Long userId;

}
