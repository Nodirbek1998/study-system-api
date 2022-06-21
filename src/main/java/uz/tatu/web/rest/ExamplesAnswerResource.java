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
import uz.tatu.repository.ExamplesAnswerRepository;
import uz.tatu.service.ExamplesAnswerService;
import uz.tatu.service.custom.ExamplesAnswerListDTO;
import uz.tatu.service.dto.ExamplesAnswerDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * REST controller for managing {@link uz.tatu.domain.ExamplesAnswer}.
 */
@RestController
@RequestMapping("/api")
public class ExamplesAnswerResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    private static final String ENTITY_NAME = "examples";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamplesAnswerService examplesAnswerService;

    private final ExamplesAnswerRepository examplesAnswerRepository;

    public ExamplesAnswerResource(ExamplesAnswerService examplesAnswerService, ExamplesAnswerRepository examplesAnswerRepository) {
        this.examplesAnswerService = examplesAnswerService;
        this.examplesAnswerRepository = examplesAnswerRepository;
    }


    /**
     * {@code POST  /examples-answer} : Create a new examplesAnswer.
     *
     * @param examplesAnswerDTO the examplesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examplesAnswerDTO, or with status {@code 400 (Bad Request)} if the examplesAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/examples-answer")
    public ResponseEntity<ExamplesAnswerDTO> createExamples(@Valid @RequestBody ExamplesAnswerDTO examplesAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save ExamplesAnswer : {}", examplesAnswerDTO);
        if (examplesAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new examples cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamplesAnswerDTO result = examplesAnswerService.save(examplesAnswerDTO);
        return ResponseEntity
                .created(new URI("/api/examples-answer/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /examples-answer/:id} : Updates an existing examplesAnswer.
     *
     * @param id the id of the examplesAnswerDTO to save.
     * @param examplesAnswerDTO the examplesAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examplesAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the examplesAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examplesAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/examples-answer/{id}")
    public ResponseEntity<ExamplesAnswerDTO> updateExamples(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody ExamplesAnswerDTO examplesAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update examplesAnswer : {}, {}", id, examplesAnswerDTO);
        if (examplesAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examplesAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examplesAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamplesAnswerDTO result = examplesAnswerService.save(examplesAnswerDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examplesAnswerDTO.getId().toString()))
                .body(result);
    }


    /**
     * {@code GET  /examples-answer} : get all the examplesAnswer.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examplesAnswer in body.
     */
    @GetMapping("/examples-answer")
    public ResponseEntity<List<ExamplesAnswerListDTO>> getAllExamplesAnswer(@org.springdoc.api.annotations.ParameterObject Pageable pageable, @RequestParam MultiValueMap< String , String > queryParam) {
        log.debug("REST request to get a page of ExamplesAnswer");
        Page<ExamplesAnswerListDTO> page = examplesAnswerService.findAll(pageable, queryParam);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /examples-answer/:id} : get the "id" examplesAnswer.
     *
     * @param id the id of the examplesAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examplesAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/examples-answer/{id}")
    public ResponseEntity<ExamplesAnswerDTO> getExamplesAnswer(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Optional<ExamplesAnswerDTO> examplesAnswerDTO = examplesAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examplesAnswerDTO);
    }

    /**
     * {@code DELETE  /examples-answer/:id} : delete the "id" examplesAnswer.
     *
     * @param id the id of the examplesAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/examples-answer/{id}")
    public ResponseEntity<Void> deleteExamplesAnswer(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        examplesAnswerService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

}
