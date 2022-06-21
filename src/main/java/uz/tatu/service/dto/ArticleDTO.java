package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.tatu.domain.Article} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class ArticleDTO implements Serializable {

    private Long id;

    private String name;

    @Size(max = 1000)
    private String text;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Long usersId;

    private Long createdById;

    private Long updatedById;

    private ImagesDTO imagesDTO;

    private String status;

}
