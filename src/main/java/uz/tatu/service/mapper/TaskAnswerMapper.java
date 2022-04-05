package uz.tatu.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import uz.tatu.domain.TaskAnswer;
import uz.tatu.service.dto.TaskAnswerDTO;

/**
 * Mapper for the entity {@link TaskAnswer} and its DTO {@link TaskAnswerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TaskAnswerMapper extends EntityMapper<TaskAnswerDTO, TaskAnswer> {

    default TaskAnswer fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskAnswer taskAnswer = new TaskAnswer();
        taskAnswer.setId(id);
        return taskAnswer;
    }
}
