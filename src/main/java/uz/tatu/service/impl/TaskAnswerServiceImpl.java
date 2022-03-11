package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.TaskAnswer;
import uz.tatu.repository.TaskAnswerRepository;
import uz.tatu.service.TaskAnswerService;
import uz.tatu.service.dto.TaskAnswerDTO;
import uz.tatu.service.mapper.TaskAnswerMapper;

/**
 * Service Implementation for managing {@link TaskAnswer}.
 */
@Service
@Transactional
public class TaskAnswerServiceImpl implements TaskAnswerService {

    private final Logger log = LoggerFactory.getLogger(TaskAnswerServiceImpl.class);

    private final TaskAnswerRepository taskAnswerRepository;

    private final TaskAnswerMapper taskAnswerMapper;

    public TaskAnswerServiceImpl(TaskAnswerRepository taskAnswerRepository, TaskAnswerMapper taskAnswerMapper) {
        this.taskAnswerRepository = taskAnswerRepository;
        this.taskAnswerMapper = taskAnswerMapper;
    }

    @Override
    public TaskAnswerDTO save(TaskAnswerDTO taskAnswerDTO) {
        log.debug("Request to save TaskAnswer : {}", taskAnswerDTO);
        TaskAnswer taskAnswer = taskAnswerMapper.toEntity(taskAnswerDTO);
        taskAnswer = taskAnswerRepository.save(taskAnswer);
        return taskAnswerMapper.toDto(taskAnswer);
    }

    @Override
    public Optional<TaskAnswerDTO> partialUpdate(TaskAnswerDTO taskAnswerDTO) {
        log.debug("Request to partially update TaskAnswer : {}", taskAnswerDTO);

        return taskAnswerRepository
            .findById(taskAnswerDTO.getId())
            .map(existingTaskAnswer -> {
                taskAnswerMapper.partialUpdate(existingTaskAnswer, taskAnswerDTO);

                return existingTaskAnswer;
            })
            .map(taskAnswerRepository::save)
            .map(taskAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaskAnswers");
        return taskAnswerRepository.findAll(pageable).map(taskAnswerMapper::toDto);
    }

    public Page<TaskAnswerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taskAnswerRepository.findAllWithEagerRelationships(pageable).map(taskAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskAnswerDTO> findOne(Long id) {
        log.debug("Request to get TaskAnswer : {}", id);
        return taskAnswerRepository.findOneWithEagerRelationships(id).map(taskAnswerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaskAnswer : {}", id);
        taskAnswerRepository.deleteById(id);
    }
}
