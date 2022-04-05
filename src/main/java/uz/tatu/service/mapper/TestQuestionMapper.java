package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.TestQuestion;
import uz.tatu.domain.Tests;
import uz.tatu.service.dto.TestQuestionDTO;

/**
 * Mapper for the entity {@link TestQuestion} and its DTO {@link TestQuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { TestsMapper.class })
public interface TestQuestionMapper extends EntityMapper<TestQuestionDTO, TestQuestion> {

    @Mapping(source = "test.id", target = "testId")
    TestQuestionDTO toDto(TestQuestion s);

    @Mapping(source = "testId", target = "test")
    TestQuestion toEntity(TestQuestionDTO s);

    default TestQuestion fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setId(id);
        return testQuestion;
    }
}
