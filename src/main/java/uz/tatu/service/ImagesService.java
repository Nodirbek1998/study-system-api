package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.tatu.service.dto.ImagesDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.Images}.
 */
public interface ImagesService {
    /**
     * Save a images.
     *
     * @param imagesDTO the entity to save.
     * @return the persisted entity.
     */
    ImagesDTO save(ImagesDTO imagesDTO);

    /**
     * Partially updates a images.
     *
     * @param imagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImagesDTO> partialUpdate(ImagesDTO imagesDTO);

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImagesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" images.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImagesDTO> findOne(Long id);

    /**
     * Delete the "id" images.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
