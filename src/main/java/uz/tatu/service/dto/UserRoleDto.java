package uz.tatu.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class UserRoleDto {

    private Long id;

    private Long userId;

    private Long roleId;
}
