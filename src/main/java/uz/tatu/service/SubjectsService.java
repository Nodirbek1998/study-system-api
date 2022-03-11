package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.SubjectsDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.Subjects}.
 */
public interface SubjectsService {
    /**
     * Save a subjects.
     *
     * @param subjectsDTO the entity to save.
     * @return the persisted entity.
     */
    SubjectsDTO save(SubjectsDTO subjectsDTO);

    /**
     * Partially updates a subjects.
     *
     * @param subjectsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubjectsDTO> partialUpdate(SubjectsDTO subjectsDTO);

    /**
     * Get all the subjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubjectsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subjects.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubjectsDTO> findOne(Long id);

    /**
     * Delete the "id" subjects.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
