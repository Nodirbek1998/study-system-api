package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@ToString
public class ExamplesAnswerDTO {

    private Long id;

    private String name;

    private Long createdBy;

    private Long filesId;

    private Long examplesId;

    private String createdAt;

    private FilesDTO filesDTO;

    private ExamplesDTO examplesDTO;

    private Double ball;

}
