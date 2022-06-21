package uz.tatu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Examples;
import uz.tatu.service.custom.ExamplesListDTO;
import uz.tatu.service.custom.GroupListDTO;
import uz.tatu.service.dto.ExamplesDTO;
import uz.tatu.service.dto.GroupsDTO;

import java.util.Optional;

public interface ExamplesService {

    ExamplesDTO save(ExamplesDTO examplesDTO);

    /**
     * Get all the examples.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamplesListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams);

    /**
     * Get all the examples with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamplesDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" examples.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamplesDTO> findOne(Long id);

    /**
     * Delete the "id" examples.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
