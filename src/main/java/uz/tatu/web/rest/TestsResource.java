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
import uz.tatu.repository.TestsRepository;
import uz.tatu.service.TestsService;
import uz.tatu.service.dto.TestsDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.Tests}.
 */
@RestController
@RequestMapping("/api")
public class TestsResource {

    private final Logger log = LoggerFactory.getLogger(TestsResource.class);

    private static final String ENTITY_NAME = "tests";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestsService testsService;

    private final TestsRepository testsRepository;

    public TestsResource(TestsService testsService, TestsRepository testsRepository) {
        this.testsService = testsService;
        this.testsRepository = testsRepository;
    }

    /**
     * {@code POST  /tests} : Create a new tests.
     *
     * @param testsDTO the testsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testsDTO, or with status {@code 400 (Bad Request)} if the tests has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tests")
    public ResponseEntity<TestsDTO> createTests(@RequestBody TestsDTO testsDTO) throws URISyntaxException {
        log.debug("REST request to save Tests : {}", testsDTO);
        if (testsDTO.getId() != null) {
            throw new BadRequestAlertException("A new tests cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestsDTO result = testsService.save(testsDTO);
        return ResponseEntity
            .created(new URI("/api/tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tests/:id} : Updates an existing tests.
     *
     * @param id the id of the testsDTO to save.
     * @param testsDTO the testsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testsDTO,
     * or with status {@code 400 (Bad Request)} if the testsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tests/{id}")
    public ResponseEntity<TestsDTO> updateTests(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestsDTO testsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tests : {}, {}", id, testsDTO);
        if (testsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestsDTO result = testsService.save(testsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tests/:id} : Partial updates given fields of an existing tests, field will ignore if it is null
     *
     * @param id the id of the testsDTO to save.
     * @param testsDTO the testsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testsDTO,
     * or with status {@code 400 (Bad Request)} if the testsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestsDTO> partialUpdateTests(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestsDTO testsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tests partially : {}, {}", id, testsDTO);
        if (testsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestsDTO> result = testsService.partialUpdate(testsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tests} : get all the tests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tests in body.
     */
    @GetMapping("/tests")
    public ResponseEntity<List<TestsDTO>> getAllTests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tests");
        Page<TestsDTO> page = testsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tests/:id} : get the "id" tests.
     *
     * @param id the id of the testsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tests/{id}")
    public ResponseEntity<TestsDTO> getTests(@PathVariable Long id) {
        log.debug("REST request to get Tests : {}", id);
        Optional<TestsDTO> testsDTO = testsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testsDTO);
    }

    /**
     * {@code DELETE  /tests/:id} : delete the "id" tests.
     *
     * @param id the id of the testsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tests/{id}")
    public ResponseEntity<Void> deleteTests(@PathVariable Long id) {
        log.debug("REST request to delete Tests : {}", id);
        testsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
