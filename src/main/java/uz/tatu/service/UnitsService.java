package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.custom.UnitsListDTO;
import uz.tatu.service.dto.UnitsDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.Units}.
 */
public interface UnitsService {
    /**
     * Save a units.
     *
     * @param unitsDTO the entity to save.
     * @return the persisted entity.
     */
    UnitsDTO save(UnitsDTO unitsDTO);

    /**
     * Partially updates a units.
     *
     * @param unitsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UnitsDTO> partialUpdate(UnitsDTO unitsDTO);

    /**
     * Get all the units.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UnitsListDTO> findAll(Pageable pageable, MultiValueMap<String , String> queryParam);

    /**
     * Get the "id" units.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnitsDTO> findOne(Long id);

    /**
     * Delete the "id" units.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
