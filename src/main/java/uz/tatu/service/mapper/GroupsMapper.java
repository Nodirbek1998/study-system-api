package uz.tatu.service.mapper;

;
import org.mapstruct.*;
import uz.tatu.domain.Groups;
import uz.tatu.service.dto.GroupsDTO;

/**
 * Mapper for the entity {@link Groups} and its DTO {@link GroupsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {

    GroupsDTO toDto(Groups s);

    Groups toEntity(GroupsDTO groupsDTO);

    default Groups fromId(Long id) {
        if (id == null) {
            return null;
        }
        Groups groups = new Groups();
        groups.setId(id);
        return groups;
    }
}
