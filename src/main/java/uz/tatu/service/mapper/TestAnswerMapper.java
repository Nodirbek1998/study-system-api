package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.TestAnswer;
import uz.tatu.service.dto.TestAnswerDTO;

/**
 * Mapper for the entity {@link TestAnswer} and its DTO {@link TestAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TestAnswerMapper extends EntityMapper<TestAnswerDTO, TestAnswer> {

    default TestAnswer fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestAnswer testAnswer = new TestAnswer();
        testAnswer.setId(id);
        return testAnswer;
    }
}
