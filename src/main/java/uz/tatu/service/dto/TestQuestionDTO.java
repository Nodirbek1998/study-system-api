package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.TestQuestion} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class TestQuestionDTO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private Boolean answerA;

    private Boolean answerB;

    private Boolean answerC;

    private Boolean answerD;

    private Long testId;

}
