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
import uz.tatu.repository.StudyLogsRepository;
import uz.tatu.service.StudyLogsService;
import uz.tatu.service.dto.StudyLogsDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.StudyLogs}.
 */
@RestController
@RequestMapping("/api")
public class StudyLogsResource {

    private final Logger log = LoggerFactory.getLogger(StudyLogsResource.class);

    private static final String ENTITY_NAME = "studyLogs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyLogsService studyLogsService;

    private final StudyLogsRepository studyLogsRepository;

    public StudyLogsResource(StudyLogsService studyLogsService, StudyLogsRepository studyLogsRepository) {
        this.studyLogsService = studyLogsService;
        this.studyLogsRepository = studyLogsRepository;
    }

    /**
     * {@code POST  /study-logs} : Create a new studyLogs.
     *
     * @param studyLogsDTO the studyLogsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyLogsDTO, or with status {@code 400 (Bad Request)} if the studyLogs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study-logs")
    public ResponseEntity<StudyLogsDTO> createStudyLogs(@RequestBody StudyLogsDTO studyLogsDTO) throws URISyntaxException {
        log.debug("REST request to save StudyLogs : {}", studyLogsDTO);
        if (studyLogsDTO.getId() != null) {
            throw new BadRequestAlertException("A new studyLogs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudyLogsDTO result = studyLogsService.save(studyLogsDTO);
        return ResponseEntity
            .created(new URI("/api/study-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-logs/:id} : Updates an existing studyLogs.
     *
     * @param id the id of the studyLogsDTO to save.
     * @param studyLogsDTO the studyLogsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyLogsDTO,
     * or with status {@code 400 (Bad Request)} if the studyLogsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyLogsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study-logs/{id}")
    public ResponseEntity<StudyLogsDTO> updateStudyLogs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudyLogsDTO studyLogsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StudyLogs : {}, {}", id, studyLogsDTO);
        if (studyLogsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyLogsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyLogsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudyLogsDTO result = studyLogsService.save(studyLogsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyLogsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /study-logs/:id} : Partial updates given fields of an existing studyLogs, field will ignore if it is null
     *
     * @param id the id of the studyLogsDTO to save.
     * @param studyLogsDTO the studyLogsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyLogsDTO,
     * or with status {@code 400 (Bad Request)} if the studyLogsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studyLogsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studyLogsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/study-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudyLogsDTO> partialUpdateStudyLogs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudyLogsDTO studyLogsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudyLogs partially : {}, {}", id, studyLogsDTO);
        if (studyLogsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyLogsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyLogsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudyLogsDTO> result = studyLogsService.partialUpdate(studyLogsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyLogsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /study-logs} : get all the studyLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studyLogs in body.
     */
    @GetMapping("/study-logs")
    public ResponseEntity<List<StudyLogsDTO>> getAllStudyLogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StudyLogs");
        Page<StudyLogsDTO> page = studyLogsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /study-logs/:id} : get the "id" studyLogs.
     *
     * @param id the id of the studyLogsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyLogsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study-logs/{id}")
    public ResponseEntity<StudyLogsDTO> getStudyLogs(@PathVariable Long id) {
        log.debug("REST request to get StudyLogs : {}", id);
        Optional<StudyLogsDTO> studyLogsDTO = studyLogsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studyLogsDTO);
    }

    /**
     * {@code DELETE  /study-logs/:id} : delete the "id" studyLogs.
     *
     * @param id the id of the studyLogsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study-logs/{id}")
    public ResponseEntity<Void> deleteStudyLogs(@PathVariable Long id) {
        log.debug("REST request to delete StudyLogs : {}", id);
        studyLogsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
