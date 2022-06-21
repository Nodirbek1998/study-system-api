package uz.tatu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.custom.ExamplesAnswerListDTO;
import uz.tatu.service.dto.ExamplesAnswerDTO;

import java.util.Optional;

public interface ExamplesAnswerService {

    ExamplesAnswerDTO save(ExamplesAnswerDTO examplesAnswerDTO);

    /**
     * Get all the examplesAnswer.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamplesAnswerListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams);

    /**
     * Get all the examplesAnswer with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExamplesAnswerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" examplesAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExamplesAnswerDTO> findOne(Long id);

    /**
     * Delete the "id" examplesAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
