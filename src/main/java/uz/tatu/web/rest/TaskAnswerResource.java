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
import uz.tatu.repository.TaskAnswerRepository;
import uz.tatu.service.TaskAnswerService;
import uz.tatu.service.dto.TaskAnswerDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.TaskAnswer}.
 */
@RestController
@RequestMapping("/api")
public class TaskAnswerResource {

    private final Logger log = LoggerFactory.getLogger(TaskAnswerResource.class);

    private static final String ENTITY_NAME = "taskAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskAnswerService taskAnswerService;

    private final TaskAnswerRepository taskAnswerRepository;

    public TaskAnswerResource(TaskAnswerService taskAnswerService, TaskAnswerRepository taskAnswerRepository) {
        this.taskAnswerService = taskAnswerService;
        this.taskAnswerRepository = taskAnswerRepository;
    }

    /**
     * {@code POST  /task-answers} : Create a new taskAnswer.
     *
     * @param taskAnswerDTO the taskAnswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskAnswerDTO, or with status {@code 400 (Bad Request)} if the taskAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-answers")
    public ResponseEntity<TaskAnswerDTO> createTaskAnswer(@RequestBody TaskAnswerDTO taskAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save TaskAnswer : {}", taskAnswerDTO);
        if (taskAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaskAnswerDTO result = taskAnswerService.save(taskAnswerDTO);
        return ResponseEntity
            .created(new URI("/api/task-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /task-answers/:id} : Updates an existing taskAnswer.
     *
     * @param id the id of the taskAnswerDTO to save.
     * @param taskAnswerDTO the taskAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the taskAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-answers/{id}")
    public ResponseEntity<TaskAnswerDTO> updateTaskAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskAnswerDTO taskAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaskAnswer : {}, {}", id, taskAnswerDTO);
        if (taskAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskAnswerDTO result = taskAnswerService.save(taskAnswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /task-answers/:id} : Partial updates given fields of an existing taskAnswer, field will ignore if it is null
     *
     * @param id the id of the taskAnswerDTO to save.
     * @param taskAnswerDTO the taskAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the taskAnswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskAnswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/task-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskAnswerDTO> partialUpdateTaskAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaskAnswerDTO taskAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaskAnswer partially : {}, {}", id, taskAnswerDTO);
        if (taskAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskAnswerDTO> result = taskAnswerService.partialUpdate(taskAnswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskAnswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-answers} : get all the taskAnswers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskAnswers in body.
     */
    @GetMapping("/task-answers")
    public ResponseEntity<List<TaskAnswerDTO>> getAllTaskAnswers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TaskAnswers");
        Page<TaskAnswerDTO> page;
        if (eagerload) {
            page = taskAnswerService.findAllWithEagerRelationships(pageable);
        } else {
            page = taskAnswerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /task-answers/:id} : get the "id" taskAnswer.
     *
     * @param id the id of the taskAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/task-answers/{id}")
    public ResponseEntity<TaskAnswerDTO> getTaskAnswer(@PathVariable Long id) {
        log.debug("REST request to get TaskAnswer : {}", id);
        Optional<TaskAnswerDTO> taskAnswerDTO = taskAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskAnswerDTO);
    }

    /**
     * {@code DELETE  /task-answers/:id} : delete the "id" taskAnswer.
     *
     * @param id the id of the taskAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/task-answers/{id}")
    public ResponseEntity<Void> deleteTaskAnswer(@PathVariable Long id) {
        log.debug("REST request to delete TaskAnswer : {}", id);
        taskAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
