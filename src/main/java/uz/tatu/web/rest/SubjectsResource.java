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
import uz.tatu.repository.SubjectsRepository;
import uz.tatu.service.SubjectsService;
import uz.tatu.service.dto.SubjectsDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.Subjects}.
 */
@RestController
@RequestMapping("/api")
public class SubjectsResource {

    private final Logger log = LoggerFactory.getLogger(SubjectsResource.class);

    private static final String ENTITY_NAME = "subjects";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubjectsService subjectsService;

    private final SubjectsRepository subjectsRepository;

    public SubjectsResource(SubjectsService subjectsService, SubjectsRepository subjectsRepository) {
        this.subjectsService = subjectsService;
        this.subjectsRepository = subjectsRepository;
    }

    /**
     * {@code POST  /subjects} : Create a new subjects.
     *
     * @param subjectsDTO the subjectsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subjectsDTO, or with status {@code 400 (Bad Request)} if the subjects has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subjects")
    public ResponseEntity<SubjectsDTO> createSubjects(@RequestBody SubjectsDTO subjectsDTO) throws URISyntaxException {
        log.debug("REST request to save Subjects : {}", subjectsDTO);
        if (subjectsDTO.getId() != null) {
            throw new BadRequestAlertException("A new subjects cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubjectsDTO result = subjectsService.save(subjectsDTO);
        return ResponseEntity
            .created(new URI("/api/subjects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subjects/:id} : Updates an existing subjects.
     *
     * @param id the id of the subjectsDTO to save.
     * @param subjectsDTO the subjectsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectsDTO,
     * or with status {@code 400 (Bad Request)} if the subjectsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subjectsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subjects/{id}")
    public ResponseEntity<SubjectsDTO> updateSubjects(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubjectsDTO subjectsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Subjects : {}, {}", id, subjectsDTO);
        if (subjectsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subjectsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subjectsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubjectsDTO result = subjectsService.save(subjectsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subjectsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subjects/:id} : Partial updates given fields of an existing subjects, field will ignore if it is null
     *
     * @param id the id of the subjectsDTO to save.
     * @param subjectsDTO the subjectsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectsDTO,
     * or with status {@code 400 (Bad Request)} if the subjectsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subjectsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subjectsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subjects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubjectsDTO> partialUpdateSubjects(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubjectsDTO subjectsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subjects partially : {}, {}", id, subjectsDTO);
        if (subjectsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subjectsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subjectsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubjectsDTO> result = subjectsService.partialUpdate(subjectsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subjectsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subjects} : get all the subjects.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subjects in body.
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectsDTO>> getAllSubjects(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Subjects");
        Page<SubjectsDTO> page = subjectsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subjects/:id} : get the "id" subjects.
     *
     * @param id the id of the subjectsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subjectsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subjects/{id}")
    public ResponseEntity<SubjectsDTO> getSubjects(@PathVariable Long id) {
        log.debug("REST request to get Subjects : {}", id);
        Optional<SubjectsDTO> subjectsDTO = subjectsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subjectsDTO);
    }

    /**
     * {@code DELETE  /subjects/:id} : delete the "id" subjects.
     *
     * @param id the id of the subjectsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<Void> deleteSubjects(@PathVariable Long id) {
        log.debug("REST request to delete Subjects : {}", id);
        subjectsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
