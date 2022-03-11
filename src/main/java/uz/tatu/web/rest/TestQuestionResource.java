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
import uz.tatu.repository.TestQuestionRepository;
import uz.tatu.service.TestQuestionService;
import uz.tatu.service.dto.TestQuestionDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.TestQuestion}.
 */
@RestController
@RequestMapping("/api")
public class TestQuestionResource {

    private final Logger log = LoggerFactory.getLogger(TestQuestionResource.class);

    private static final String ENTITY_NAME = "testQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestQuestionService testQuestionService;

    private final TestQuestionRepository testQuestionRepository;

    public TestQuestionResource(TestQuestionService testQuestionService, TestQuestionRepository testQuestionRepository) {
        this.testQuestionService = testQuestionService;
        this.testQuestionRepository = testQuestionRepository;
    }

    /**
     * {@code POST  /test-questions} : Create a new testQuestion.
     *
     * @param testQuestionDTO the testQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testQuestionDTO, or with status {@code 400 (Bad Request)} if the testQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-questions")
    public ResponseEntity<TestQuestionDTO> createTestQuestion(@RequestBody TestQuestionDTO testQuestionDTO) throws URISyntaxException {
        log.debug("REST request to save TestQuestion : {}", testQuestionDTO);
        if (testQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new testQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestQuestionDTO result = testQuestionService.save(testQuestionDTO);
        return ResponseEntity
            .created(new URI("/api/test-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-questions/:id} : Updates an existing testQuestion.
     *
     * @param id the id of the testQuestionDTO to save.
     * @param testQuestionDTO the testQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the testQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-questions/{id}")
    public ResponseEntity<TestQuestionDTO> updateTestQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestQuestionDTO testQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TestQuestion : {}, {}", id, testQuestionDTO);
        if (testQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestQuestionDTO result = testQuestionService.save(testQuestionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testQuestionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-questions/:id} : Partial updates given fields of an existing testQuestion, field will ignore if it is null
     *
     * @param id the id of the testQuestionDTO to save.
     * @param testQuestionDTO the testQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the testQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestQuestionDTO> partialUpdateTestQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestQuestionDTO testQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestQuestion partially : {}, {}", id, testQuestionDTO);
        if (testQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestQuestionDTO> result = testQuestionService.partialUpdate(testQuestionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, testQuestionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-questions} : get all the testQuestions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testQuestions in body.
     */
    @GetMapping("/test-questions")
    public ResponseEntity<List<TestQuestionDTO>> getAllTestQuestions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TestQuestions");
        Page<TestQuestionDTO> page = testQuestionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-questions/:id} : get the "id" testQuestion.
     *
     * @param id the id of the testQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-questions/{id}")
    public ResponseEntity<TestQuestionDTO> getTestQuestion(@PathVariable Long id) {
        log.debug("REST request to get TestQuestion : {}", id);
        Optional<TestQuestionDTO> testQuestionDTO = testQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testQuestionDTO);
    }

    /**
     * {@code DELETE  /test-questions/:id} : delete the "id" testQuestion.
     *
     * @param id the id of the testQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-questions/{id}")
    public ResponseEntity<Void> deleteTestQuestion(@PathVariable Long id) {
        log.debug("REST request to delete TestQuestion : {}", id);
        testQuestionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
