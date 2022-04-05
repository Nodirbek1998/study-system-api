package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Tests;
import uz.tatu.service.dto.TestsDTO;

/**
 * Mapper for the entity {@link Tests} and its DTO {@link TestsDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectsMapper.class })
public interface TestsMapper extends EntityMapper<TestsDTO, Tests> {


    @Mapping(source = "subject.id", target = "subjectId")
    TestsDTO toDto(Tests s);


    @Mapping(source = "subjectId", target = "subject")
    Tests toEntity(TestsDTO s);

    default Tests fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tests tests = new Tests();
        tests.setId(id);
        return tests;
    }
}
