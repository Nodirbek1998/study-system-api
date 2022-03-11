package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.TestAnswerDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.TestAnswer}.
 */
public interface TestAnswerService {
    /**
     * Save a testAnswer.
     *
     * @param testAnswerDTO the entity to save.
     * @return the persisted entity.
     */
    TestAnswerDTO save(TestAnswerDTO testAnswerDTO);

    /**
     * Partially updates a testAnswer.
     *
     * @param testAnswerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestAnswerDTO> partialUpdate(TestAnswerDTO testAnswerDTO);

    /**
     * Get all the testAnswers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestAnswerDTO> findAll(Pageable pageable);

    /**
     * Get all the testAnswers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestAnswerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" testAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestAnswerDTO> findOne(Long id);

    /**
     * Delete the "id" testAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
