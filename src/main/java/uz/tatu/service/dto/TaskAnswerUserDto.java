package uz.tatu.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class TaskAnswerUserDto {

    private Long id;

    private Long taskAnswerId;

    private Long userId;
}
