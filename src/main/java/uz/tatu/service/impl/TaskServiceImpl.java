package uz.tatu.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Task;
import uz.tatu.repository.TaskRepository;
import uz.tatu.service.TaskService;
import uz.tatu.service.dto.TaskDTO;
import uz.tatu.service.mapper.FilesMapper;
import uz.tatu.service.mapper.TaskMapper;
import uz.tatu.service.utils.RequestUtil;

/**
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final FilesMapper filesMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, FilesMapper filesMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.filesMapper = filesMapper;
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task.setDeadline(LocalDateTime.of(taskDTO.getDeadline(), taskDTO.getTime()));
        task.setFiles(filesMapper.toEntity(taskDTO.getFilesDTO()));
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDTO> partialUpdate(TaskDTO taskDTO) {
        log.debug("Request to partially update Task : {}", taskDTO);

        return taskRepository
            .findById(taskDTO.getId())
            .map(existingTask -> {
                taskMapper.partialUpdate(existingTask, taskDTO);

                return existingTask;
            })
            .map(taskRepository::save)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll(MultiValueMap<String, String> queryParams) {
        log.debug("Request to get all Tasks");
        if (RequestUtil.checkValue(queryParams,"unitsId")){
            List<Task> unitsId = taskRepository.findAllByUnitsId(Long.valueOf(queryParams.getFirst("unitsId")));
            return unitsId;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }
}
