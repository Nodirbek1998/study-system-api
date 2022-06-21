package uz.tatu.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.TaskAnswerUser;
import uz.tatu.service.dto.TaskAnswerUserDto;

@Mapper(componentModel = "spring", uses = {TaskMapper.class, UserMapper.class, FilesMapper.class})
public interface TaskAnswerUserMapper extends EntityMapper<TaskAnswerUserDto, TaskAnswerUser> {

    @Mapping(source = "users.id", target = "userId")
    @Mapping(source = "files.id", target = "fileId")
    @Mapping(source = "task.id", target = "taskId")
    TaskAnswerUserDto toDto(TaskAnswerUser s);

    @Mapping(source = "userId", target = "users")
    @Mapping(source = "fileId", target = "files")
    @Mapping(source = "taskId", target = "task")
    TaskAnswerUser toEntity(TaskAnswerUserDto s);

    default TaskAnswerUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskAnswerUser taskAnswerUser = new TaskAnswerUser();
        taskAnswerUser.setId(id);
        return taskAnswerUser;
    }
}
