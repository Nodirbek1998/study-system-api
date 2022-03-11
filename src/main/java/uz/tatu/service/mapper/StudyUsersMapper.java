package uz.tatu.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import uz.tatu.domain.StudyUsers;
import uz.tatu.service.dto.StudyUsersDTO;

/**
 * Mapper for the entity {@link StudyUsers} and its DTO {@link StudyUsersDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface StudyUsersMapper extends EntityMapper<StudyUsersDTO, StudyUsers> {
    @Mapping(target = "role", source = "role", qualifiedByName = "id")
    StudyUsersDTO toDto(StudyUsers s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudyUsersDTO toDtoId(StudyUsers studyUsers);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<StudyUsersDTO> toDtoIdSet(Set<StudyUsers> studyUsers);
}
