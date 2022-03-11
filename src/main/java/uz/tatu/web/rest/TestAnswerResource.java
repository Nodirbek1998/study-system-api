package uz.tatu.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.tatu.repository.TestAnswerRepository;
import uz.tatu.service.TestAnswerService;
import uz.tatu.service.dto.TestAnswerDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.TestAnswer}.
 */
@RestController
@RequestMapping("/api")
public class TestAnswerResource {

    private final Logger log = LoggerFactory.getLogger(TestAnswerResource.class);

    private static final String ENTITY_NAME = "testAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestAnswerService testAnswerService;

    private final TestAnswerRepository testAnswerRepository;

    public TestAnswerResource(TestAnswerService testAnswerService, TestAnswerRepository testAnswerRepository) {
        this.testAnswerService = testAnswerService;
        this.testAnswerRepository = testAnswerRepository;
    }

    /**
     * {@code POST  /test-answers} : Create a new testAnswer.
     *
     * @param testAnswerDTO the testAnswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testAnswerDTO, or with status {@code 400 (Bad Request)} if the testAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-answers")
    public ResponseEntity<TestAnswerDTO> createTestAnswer(@RequestBody TestAnswerDTO testAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save TestAnswer : {}", testAnswerDTO);
        if (testAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new testAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestAnswerDTO result = testAnswerService.save(testAnswerDTO);
        return ResponseEntity
            .created(new URI("/api/test-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-answers/:id} : Updates an existing testAnswer.
     *
     * @param id the id of the testAnswerDTO to save.
     * @param testAnswerDTO the testAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the testAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-answers/{id}")
    public ResponseEntity<TestAnswerDTO> updateTestAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestAnswerDTO testAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TestAnswer : {}, {}", id, testAnswerDTO);
        if (testAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestAnswerDTO result = testAnswerService.save(testAnswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-answers/:id} : Partial updates given fields of an existing testAnswer, field will ignore if it is null
     *
     * @param id the id of the testAnswerDTO to save.
     * @param testAnswerDTO the testAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the testAnswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testAnswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestAnswerDTO> partialUpdateTestAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestAnswerDTO testAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestAnswer partially : {}, {}", id, testAnswerDTO);
        if (testAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestAnswerDTO> result = testAnswerService.partialUpdate(testAnswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testAnswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-answers} : get all the testAnswers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testAnswers in body.
     */
    @GetMapping("/test-answers")
    public ResponseEntity<List<TestAnswerDTO>> getAllTestAnswers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TestAnswers");
        Page<TestAnswerDTO> page;
        if (eagerload) {
            page = testAnswerService.findAllWithEagerRelationships(pageable);
        } else {
            page = testAnswerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-answers/:id} : get the "id" testAnswer.
     *
     * @param id the id of the testAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-answers/{id}")
    public ResponseEntity<TestAnswerDTO> getTestAnswer(@PathVariable Long id) {
        log.debug("REST request to get TestAnswer : {}", id);
        Optional<TestAnswerDTO> testAnswerDTO = testAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testAnswerDTO);
    }

    /**
     * {@code DELETE  /test-answers/:id} : delete the "id" testAnswer.
     *
     * @param id the id of the testAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-answers/{id}")
    public ResponseEntity<Void> deleteTestAnswer(@PathVariable Long id) {
        log.debug("REST request to delete TestAnswer : {}", id);
        testAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
