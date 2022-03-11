package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.StudyUsersDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.StudyUsers}.
 */
public interface StudyUsersService {
    /**
     * Save a studyUsers.
     *
     * @param studyUsersDTO the entity to save.
     * @return the persisted entity.
     */
    StudyUsersDTO save(StudyUsersDTO studyUsersDTO);

    /**
     * Partially updates a studyUsers.
     *
     * @param studyUsersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudyUsersDTO> partialUpdate(StudyUsersDTO studyUsersDTO);

    /**
     * Get all the studyUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudyUsersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" studyUsers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudyUsersDTO> findOne(Long id);

    /**
     * Delete the "id" studyUsers.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
