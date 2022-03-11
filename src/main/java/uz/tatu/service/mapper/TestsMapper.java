package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Tests;
import uz.tatu.service.dto.TestsDTO;

/**
 * Mapper for the entity {@link Tests} and its DTO {@link TestsDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectsMapper.class })
public interface TestsMapper extends EntityMapper<TestsDTO, Tests> {
    @Mapping(target = "subject", source = "subject", qualifiedByName = "id")
    TestsDTO toDto(Tests s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TestsDTO toDtoId(Tests tests);
}
