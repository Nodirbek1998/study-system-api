package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.TaskAnswerDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.TaskAnswer}.
 */
public interface TaskAnswerService {
    /**
     * Save a taskAnswer.
     *
     * @param taskAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    TaskAnswerDTO save(TaskAnswerDTO taskAnswerDTO);

    /**
     * Partially updates a taskAnswer.
     *
     * @param taskAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskAnswerDTO> partialUpdate(TaskAnswerDTO taskAnswerDTO);

    /**
     * Get all the taskAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskAnswerDTO> findAll(Pageable pageable);

    /**
     * Get all the taskAnswers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskAnswerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" taskAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskAnswerDTO> findOne(Long id);

    /**
     * Delete the "id" taskAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
