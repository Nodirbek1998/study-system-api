package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class GroupUsersDto {

    private Long id;

    private Long usersId;

    private Long groupsId;
}
