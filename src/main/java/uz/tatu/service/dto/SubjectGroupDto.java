package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class SubjectGroupDto {

    private Long id;

    private Long subjectId;

    private Long groupId;
}
