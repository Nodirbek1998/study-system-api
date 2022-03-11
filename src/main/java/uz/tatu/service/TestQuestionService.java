package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.TestQuestionDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.TestQuestion}.
 */
public interface TestQuestionService {
    /**
     * Save a testQuestion.
     *
     * @param testQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    TestQuestionDTO save(TestQuestionDTO testQuestionDTO);

    /**
     * Partially updates a testQuestion.
     *
     * @param testQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestQuestionDTO> partialUpdate(TestQuestionDTO testQuestionDTO);

    /**
     * Get all the testQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestQuestionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" testQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestQuestionDTO> findOne(Long id);

    /**
     * Delete the "id" testQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
