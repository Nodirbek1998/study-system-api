package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Task;
import uz.tatu.service.dto.TaskDTO;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {}
