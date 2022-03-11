package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.StudyLogsDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.StudyLogs}.
 */
public interface StudyLogsService {
    /**
     * Save a studyLogs.
     *
     * @param studyLogsDTO the entity to save.
     * @return the persisted entity.
     */
    StudyLogsDTO save(StudyLogsDTO studyLogsDTO);

    /**
     * Partially updates a studyLogs.
     *
     * @param studyLogsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudyLogsDTO> partialUpdate(StudyLogsDTO studyLogsDTO);

    /**
     * Get all the studyLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudyLogsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" studyLogs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudyLogsDTO> findOne(Long id);

    /**
     * Delete the "id" studyLogs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
