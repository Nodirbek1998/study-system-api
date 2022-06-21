package uz.tatu.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.tatu.repository.ExamplesRepository;
import uz.tatu.service.ExamplesService;
import uz.tatu.service.custom.ExamplesListDTO;
import uz.tatu.service.dto.ArticleDTO;
import uz.tatu.service.dto.ExamplesDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link uz.tatu.domain.Examples}.
 */
@RestController
@RequestMapping("/api")
public class ExamplesResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    private static final String ENTITY_NAME = "examples";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamplesService examplesService;

    private final ExamplesRepository examplesRepository;

    public ExamplesResource(ExamplesService examplesService, ExamplesRepository examplesRepository) {
        this.examplesService = examplesService;
        this.examplesRepository = examplesRepository;
    }


    /**
     * {@code POST  /examples} : Create a new examples.
     *
     * @param examplesDTO the examplesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examplesDTO, or with status {@code 400 (Bad Request)} if the examples has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/examples")
    public ResponseEntity<ExamplesDTO> createExamples(@Valid @RequestBody ExamplesDTO examplesDTO) throws URISyntaxException {
        log.debug("REST request to save Examples : {}", examplesDTO);
        if (examplesDTO.getId() != null) {
            throw new BadRequestAlertException("A new examples cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamplesDTO result = examplesService.save(examplesDTO);
        return ResponseEntity
                .created(new URI("/api/examples/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /examples/:id} : Updates an existing examples.
     *
     * @param id the id of the examplesDTO to save.
     * @param examplesDTO the examplesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examplesDTO,
     * or with status {@code 400 (Bad Request)} if the examplesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examplesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/examples/{id}")
    public ResponseEntity<ExamplesDTO> updateExamples(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ExamplesDTO examplesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update examples : {}, {}", id, examplesDTO);
        if (examplesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examplesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examplesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamplesDTO result = examplesService.save(examplesDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examplesDTO.getId().toString()))
                .body(result);
    }


    /**
     * {@code GET  /examples} : get all the examples.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examples in body.
     */
    @GetMapping("/examples")
    public ResponseEntity<List<ExamplesListDTO>> getAllExamples(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @RequestParam MultiValueMap< String , String > queryParam) {
        log.debug("REST request to get a page of Articles");
        Page<ExamplesListDTO> page = examplesService.findAll(pageable, queryParam);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /examples/:id} : get the "id" examples.
     *
     * @param id the id of the examplesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examplesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/examples/{id}")
    public ResponseEntity<ExamplesDTO> getExamples(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<ExamplesDTO> articleDTO = examplesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleDTO);
    }

    /**
     * {@code DELETE  /examples/:id} : delete the "id" examples.
     *
     * @param id the id of the examplesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/examples/{id}")
    public ResponseEntity<Void> deleteExamples(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        examplesService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }


}
