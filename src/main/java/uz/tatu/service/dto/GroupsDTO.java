package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.service.custom.GroupUserListDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * A DTO for the {@link uz.tatu.domain.Groups} entity.
 */

@Data
@EqualsAndHashCode
@ToString
public class GroupsDTO implements Serializable {

    private Long id;

    private String name;

    private String imageUrl;

    private List<GroupUserListDTO> groupsUsersList;

    private List<SubjectsDTO> subjects;

    private LocalDate createdAt;

    private LocalDate updatedAt;


}
