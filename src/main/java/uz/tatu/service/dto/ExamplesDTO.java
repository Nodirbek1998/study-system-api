package uz.tatu.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class ExamplesDTO {

    private Long id;

    private String name;

    private Long createdBy;

    private Long filesId;

    private String createdAt;

    private FilesDTO filesDTO;

    private Double ball;
}
