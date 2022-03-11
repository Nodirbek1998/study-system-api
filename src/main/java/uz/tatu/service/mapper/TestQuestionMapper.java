package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.TestQuestion;
import uz.tatu.service.dto.TestQuestionDTO;

/**
 * Mapper for the entity {@link TestQuestion} and its DTO {@link TestQuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { TestsMapper.class })
public interface TestQuestionMapper extends EntityMapper<TestQuestionDTO, TestQuestion> {
    @Mapping(target = "test", source = "test", qualifiedByName = "id")
    TestQuestionDTO toDto(TestQuestion s);
}
