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
import uz.tatu.repository.StudyUsersRepository;
import uz.tatu.service.StudyUsersService;
import uz.tatu.service.dto.StudyUsersDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.StudyUsers}.
 */
@RestController
@RequestMapping("/api")
public class StudyUsersResource {

    private final Logger log = LoggerFactory.getLogger(StudyUsersResource.class);

    private static final String ENTITY_NAME = "studyUsers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyUsersService studyUsersService;

    private final StudyUsersRepository studyUsersRepository;

    public StudyUsersResource(StudyUsersService studyUsersService, StudyUsersRepository studyUsersRepository) {
        this.studyUsersService = studyUsersService;
        this.studyUsersRepository = studyUsersRepository;
    }

    /**
     * {@code POST  /study-users} : Create a new studyUsers.
     *
     * @param studyUsersDTO the studyUsersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyUsersDTO, or with status {@code 400 (Bad Request)} if the studyUsers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/study-users")
    public ResponseEntity<StudyUsersDTO> createStudyUsers(@RequestBody StudyUsersDTO studyUsersDTO) throws URISyntaxException {
        log.debug("REST request to save StudyUsers : {}", studyUsersDTO);
        if (studyUsersDTO.getId() != null) {
            throw new BadRequestAlertException("A new studyUsers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudyUsersDTO result = studyUsersService.save(studyUsersDTO);
        return ResponseEntity
            .created(new URI("/api/study-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-users/:id} : Updates an existing studyUsers.
     *
     * @param id the id of the studyUsersDTO to save.
     * @param studyUsersDTO the studyUsersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyUsersDTO,
     * or with status {@code 400 (Bad Request)} if the studyUsersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyUsersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/study-users/{id}")
    public ResponseEntity<StudyUsersDTO> updateStudyUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudyUsersDTO studyUsersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StudyUsers : {}, {}", id, studyUsersDTO);
        if (studyUsersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyUsersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudyUsersDTO result = studyUsersService.save(studyUsersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studyUsersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /study-users/:id} : Partial updates given fields of an existing studyUsers, field will ignore if it is null
     *
     * @param id the id of the studyUsersDTO to save.
     * @param studyUsersDTO the studyUsersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyUsersDTO,
     * or with status {@code 400 (Bad Request)} if the studyUsersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studyUsersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studyUsersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/study-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudyUsersDTO> partialUpdateStudyUsers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StudyUsersDTO studyUsersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudyUsers partially : {}, {}", id, studyUsersDTO);
        if (studyUsersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyUsersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyUsersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudyUsersDTO> result = studyUsersService.partialUpdate(studyUsersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studyUsersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /study-users} : get all the studyUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studyUsers in body.
     */
    @GetMapping("/study-users")
    public ResponseEntity<List<StudyUsersDTO>> getAllStudyUsers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StudyUsers");
        Page<StudyUsersDTO> page = studyUsersService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /study-users/:id} : get the "id" studyUsers.
     *
     * @param id the id of the studyUsersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyUsersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/study-users/{id}")
    public ResponseEntity<StudyUsersDTO> getStudyUsers(@PathVariable Long id) {
        log.debug("REST request to get StudyUsers : {}", id);
        Optional<StudyUsersDTO> studyUsersDTO = studyUsersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studyUsersDTO);
    }

    /**
     * {@code DELETE  /study-users/:id} : delete the "id" studyUsers.
     *
     * @param id the id of the studyUsersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/study-users/{id}")
    public ResponseEntity<Void> deleteStudyUsers(@PathVariable Long id) {
        log.debug("REST request to delete StudyUsers : {}", id);
        studyUsersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
