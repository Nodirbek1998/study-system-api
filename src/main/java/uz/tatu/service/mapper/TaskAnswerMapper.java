package uz.tatu.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import uz.tatu.domain.TaskAnswer;
import uz.tatu.service.dto.TaskAnswerDTO;

/**
 * Mapper for the entity {@link TaskAnswer} and its DTO {@link TaskAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class })
public interface TaskAnswerMapper extends EntityMapper<TaskAnswerDTO, TaskAnswer> {
    @Mapping(target = "studyUsers", source = "studyUsers", qualifiedByName = "idSet")
    TaskAnswerDTO toDto(TaskAnswer s);

    @Mapping(target = "removeStudyUser", ignore = true)
    TaskAnswer toEntity(TaskAnswerDTO taskAnswerDTO);
}
