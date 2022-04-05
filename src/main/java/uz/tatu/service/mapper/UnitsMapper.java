package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Units;
import uz.tatu.service.dto.UnitsDTO;

/**
 * Mapper for the entity {@link Units} and its DTO {@link UnitsDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectsMapper.class })
public interface UnitsMapper extends EntityMapper<UnitsDTO, Units> {

    @Mapping(source = "subjects.id", target = "subjectId")
    UnitsDTO toDto(Units s);

    @Mapping(source = "subjectId", target = "subjects")
    Units toEntity(UnitsDTO s);

    default Units fromId(Long id) {
        if (id == null) {
            return null;
        }
        Units units = new Units();
        units.setId(id);
        return units;
    }
}
