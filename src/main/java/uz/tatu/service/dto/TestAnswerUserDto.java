package uz.tatu.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class TestAnswerUserDto {

    private Long id;

    private Long testAnswerId;

    private Long userId;
}
