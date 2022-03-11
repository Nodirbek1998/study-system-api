package uz.tatu.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import uz.tatu.domain.Subjects;
import uz.tatu.service.dto.SubjectsDTO;

/**
 * Mapper for the entity {@link Subjects} and its DTO {@link SubjectsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubjectsMapper extends EntityMapper<SubjectsDTO, Subjects> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubjectsDTO toDtoId(Subjects subjects);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SubjectsDTO> toDtoIdSet(Set<Subjects> subjects);
}
