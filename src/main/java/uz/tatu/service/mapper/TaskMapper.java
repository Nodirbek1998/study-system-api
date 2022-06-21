package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Task;
import uz.tatu.domain.TestAnswerUser;
import uz.tatu.service.dto.TaskDTO;
import uz.tatu.service.dto.TestAnswerUserDto;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = {FilesMapper.class, UnitsMapper.class})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

    @Mapping(source = "files.id", target = "fileId")
    @Mapping(source = "units.id", target = "unitsId")
    TaskDTO toDto(Task s);

    @Mapping(source = "fileId", target = "files")
    @Mapping(source = "unitsId", target = "units")
    Task toEntity(TaskDTO s);


    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
