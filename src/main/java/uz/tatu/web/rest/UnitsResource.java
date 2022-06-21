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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.tatu.repository.UnitsRepository;
import uz.tatu.service.UnitsService;
import uz.tatu.service.custom.UnitsListDTO;
import uz.tatu.service.dto.UnitsDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.Units}.
 */
@RestController
@RequestMapping("/api")
public class UnitsResource {

    private final Logger log = LoggerFactory.getLogger(UnitsResource.class);

    private static final String ENTITY_NAME = "units";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnitsService unitsService;

    private final UnitsRepository unitsRepository;

    public UnitsResource(UnitsService unitsService, UnitsRepository unitsRepository) {
        this.unitsService = unitsService;
        this.unitsRepository = unitsRepository;
    }

    /**
     * {@code POST  /units} : Create a new units.
     *
     * @param unitsDTO the unitsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unitsDTO, or with status {@code 400 (Bad Request)} if the units has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/units")
    public ResponseEntity<UnitsDTO> createUnits(@RequestBody UnitsDTO unitsDTO) throws URISyntaxException {
        log.debug("REST request to save Units : {}", unitsDTO);
        if (unitsDTO.getId() != null) {
            throw new BadRequestAlertException("A new units cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnitsDTO result = unitsService.save(unitsDTO);
        return ResponseEntity
            .created(new URI("/api/units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /units/:id} : Updates an existing units.
     *
     * @param id the id of the unitsDTO to save.
     * @param unitsDTO the unitsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitsDTO,
     * or with status {@code 400 (Bad Request)} if the unitsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unitsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/units/{id}")
    public ResponseEntity<UnitsDTO> updateUnits(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UnitsDTO unitsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Units : {}, {}", id, unitsDTO);
        if (unitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UnitsDTO result = unitsService.save(unitsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /units/:id} : Partial updates given fields of an existing units, field will ignore if it is null
     *
     * @param id the id of the unitsDTO to save.
     * @param unitsDTO the unitsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unitsDTO,
     * or with status {@code 400 (Bad Request)} if the unitsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the unitsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the unitsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/units/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UnitsDTO> partialUpdateUnits(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UnitsDTO unitsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Units partially : {}, {}", id, unitsDTO);
        if (unitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unitsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unitsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UnitsDTO> result = unitsService.partialUpdate(unitsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unitsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /units} : get all the units.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of units in body.
     */
    @GetMapping("/units")
    public ResponseEntity<Page<UnitsListDTO>> getAllUnits(Pageable pageable, @RequestParam MultiValueMap<String , String> queryParam) {
        log.debug("REST request to get a page of Units");
        Page<UnitsListDTO> page = unitsService.findAll(pageable, queryParam);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * {@code GET  /units/:id} : get the "id" units.
     *
     * @param id the id of the unitsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unitsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/units/{id}")
    public ResponseEntity<UnitsDTO> getUnits(@PathVariable Long id) {
        log.debug("REST request to get Units : {}", id);
        Optional<UnitsDTO> unitsDTO = unitsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unitsDTO);
    }

    /**
     * {@code DELETE  /units/:id} : delete the "id" units.
     *
     * @param id the id of the unitsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/units/{id}")
    public ResponseEntity<Void> deleteUnits(@PathVariable Long id) {
        log.debug("REST request to delete Units : {}", id);
        unitsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
