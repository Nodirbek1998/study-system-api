package uz.tatu.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import uz.tatu.domain.Groups;
import uz.tatu.service.dto.GroupsDTO;

/**
 * Mapper for the entity {@link Groups} and its DTO {@link GroupsDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class, SubjectsMapper.class })
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {
    @Mapping(target = "studyUsers", source = "studyUsers", qualifiedByName = "idSet")
    @Mapping(target = "subjects", source = "subjects", qualifiedByName = "idSet")
    GroupsDTO toDto(Groups s);

    @Mapping(target = "removeStudyUser", ignore = true)
    @Mapping(target = "removeSubjects", ignore = true)
    Groups toEntity(GroupsDTO groupsDTO);
}
