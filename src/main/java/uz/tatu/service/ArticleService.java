package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.custom.ArticleListDTO;
import uz.tatu.service.dto.ArticleDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.Article}.
 */
public interface ArticleService {
    /**
     * Save a article.
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    ArticleDTO save(ArticleDTO articleDTO);

    /**
     * Partially updates a article.
     *
     * @param articleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArticleDTO> partialUpdate(ArticleDTO articleDTO);

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArticleListDTO> findAll(MultiValueMap<String, String > queryParam, Pageable pageable);

    /**
     * Get the "id" article.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArticleDTO> findOne(Long id);

    /**
     * Delete the "id" article.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
