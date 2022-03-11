package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Units;
import uz.tatu.service.dto.UnitsDTO;

/**
 * Mapper for the entity {@link Units} and its DTO {@link UnitsDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectsMapper.class })
public interface UnitsMapper extends EntityMapper<UnitsDTO, Units> {
    @Mapping(target = "subject", source = "subject", qualifiedByName = "id")
    UnitsDTO toDto(Units s);
}
