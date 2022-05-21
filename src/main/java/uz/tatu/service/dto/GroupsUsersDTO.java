package uz.tatu.service.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class GroupsUsersDTO {

    private Long id;

    private Long userId;

    private Long groupId;

    private String firstName;

    private String lastName;

    private String groupName;



}
