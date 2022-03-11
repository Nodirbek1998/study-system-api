package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.TestsDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.Tests}.
 */
public interface TestsService {
    /**
     * Save a tests.
     *
     * @param testsDTO the entity to save.
     * @return the persisted entity.
     */
    TestsDTO save(TestsDTO testsDTO);

    /**
     * Partially updates a tests.
     *
     * @param testsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestsDTO> partialUpdate(TestsDTO testsDTO);

    /**
     * Get all the tests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tests.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestsDTO> findOne(Long id);

    /**
     * Delete the "id" tests.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
