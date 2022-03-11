package uz.tatu.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import uz.tatu.domain.TestAnswer;
import uz.tatu.service.dto.TestAnswerDTO;

/**
 * Mapper for the entity {@link TestAnswer} and its DTO {@link TestAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class })
public interface TestAnswerMapper extends EntityMapper<TestAnswerDTO, TestAnswer> {
    @Mapping(target = "studyUsers", source = "studyUsers", qualifiedByName = "idSet")
    TestAnswerDTO toDto(TestAnswer s);

    @Mapping(target = "removeStudyUser", ignore = true)
    TestAnswer toEntity(TestAnswerDTO testAnswerDTO);
}
