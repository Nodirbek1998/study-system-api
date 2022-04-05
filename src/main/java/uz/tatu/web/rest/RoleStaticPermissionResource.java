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
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.repository.RoleStaticPermissionRepository;
import uz.tatu.service.RoleStaticPermissionService;
import uz.tatu.service.dto.RoleStaticPermissionDTO;
import uz.tatu.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.tatu.domain.RoleStaticPermission}.
 */
@RestController
@RequestMapping("/api")
public class RoleStaticPermissionResource {

    private final Logger log = LoggerFactory.getLogger(RoleStaticPermissionResource.class);

    private static final String ENTITY_NAME = "roleStaticPermission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleStaticPermissionService roleStaticPermissionService;

    private final RoleStaticPermissionRepository roleStaticPermissionRepository;

    public RoleStaticPermissionResource(
        RoleStaticPermissionService roleStaticPermissionService,
        RoleStaticPermissionRepository roleStaticPermissionRepository
    ) {
        this.roleStaticPermissionService = roleStaticPermissionService;
        this.roleStaticPermissionRepository = roleStaticPermissionRepository;
    }

    /**
     * {@code POST  /role-static-permissions} : Create a new roleStaticPermission.
     *
     * @param roleStaticPermissionDTO the roleStaticPermissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleStaticPermissionDTO, or with status {@code 400 (Bad Request)} if the roleStaticPermission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/role-static-permissions")
    public ResponseEntity<RoleStaticPermissionDTO> createRoleStaticPermission(@RequestBody RoleStaticPermissionDTO roleStaticPermissionDTO)
        throws URISyntaxException {
        log.debug("REST request to save RoleStaticPermission : {}", roleStaticPermissionDTO);
        if (roleStaticPermissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new roleStaticPermission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleStaticPermissionDTO result;
        if(roleStaticPermissionDTO.getStaticPermissions() != null){
            result = roleStaticPermissionService.saveAll(roleStaticPermissionDTO);
        } else {
            result = roleStaticPermissionService.save(roleStaticPermissionDTO);
        }
        return ResponseEntity
            .created(new URI("/api/role-static-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role-static-permissions/:id} : Updates an existing roleStaticPermission.
     *
     * @param id the id of the roleStaticPermissionDTO to save.
     * @param roleStaticPermissionDTO the roleStaticPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleStaticPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the roleStaticPermissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleStaticPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role-static-permissions/{id}")
    public ResponseEntity<RoleStaticPermissionDTO> updateRoleStaticPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleStaticPermissionDTO roleStaticPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoleStaticPermission : {}, {}", id, roleStaticPermissionDTO);
        if (roleStaticPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleStaticPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleStaticPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleStaticPermissionDTO result = roleStaticPermissionService.save(roleStaticPermissionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleStaticPermissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /role-static-permissions/:id} : Partial updates given fields of an existing roleStaticPermission, field will ignore if it is null
     *
     * @param id the id of the roleStaticPermissionDTO to save.
     * @param roleStaticPermissionDTO the roleStaticPermissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleStaticPermissionDTO,
     * or with status {@code 400 (Bad Request)} if the roleStaticPermissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleStaticPermissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleStaticPermissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/role-static-permissions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleStaticPermissionDTO> partialUpdateRoleStaticPermission(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleStaticPermissionDTO roleStaticPermissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoleStaticPermission partially : {}, {}", id, roleStaticPermissionDTO);
        if (roleStaticPermissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleStaticPermissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleStaticPermissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleStaticPermissionDTO> result = roleStaticPermissionService.partialUpdate(roleStaticPermissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleStaticPermissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /role-static-permissions} : get all the roleStaticPermissions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleStaticPermissions in body.
     */
    @GetMapping("/role-static-permissions")
    public ResponseEntity<List<RoleStaticPermissionDTO>> getAllRoleStaticPermissions(@RequestParam MultiValueMap<String, String> queryParams, Pageable pageable) {
        log.debug("REST request to get a page of RoleStaticPermissions");
        Page<RoleStaticPermissionDTO> page = roleStaticPermissionService.findAll(queryParams, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /role-static-permissions/:id} : get the "id" roleStaticPermission.
     *
     * @param id the id of the roleStaticPermissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleStaticPermissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-static-permissions/{id}")
    public ResponseEntity<RoleStaticPermissionDTO> getRoleStaticPermission(@PathVariable Long id) {
        log.debug("REST request to get RoleStaticPermission : {}", id);
        Optional<RoleStaticPermissionDTO> roleStaticPermissionDTO = roleStaticPermissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleStaticPermissionDTO);
    }

    /**
     * {@code DELETE  /role-static-permissions/:id} : delete the "id" roleStaticPermission.
     *
     * @param id the id of the roleStaticPermissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role-static-permissions/{id}")
    public ResponseEntity<Void> deleteRoleStaticPermission(@PathVariable Long id) {
        log.debug("REST request to delete RoleStaticPermission : {}", id);
        roleStaticPermissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @DeleteMapping("/role-static-permissions")
    public ResponseEntity<Void> deleteEdoDtRoleStaticPermissionByRoleAndPermission(@RequestBody RoleStaticPermissionDTO roleStaticPermissionDTO) {
        log.debug("REST request to delete RoleStaticPermission : {}", roleStaticPermissionDTO);
//        if(!userService.getAccessMethodForProduction(EnumStaticPermission.RoleStaticPermissionDelete)){
//            return ResponseEntity
//                    .status(HttpStatus.FORBIDDEN)
//                    .build();
//        }
        roleStaticPermissionService.deleteByDTO(roleStaticPermissionDTO);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, roleStaticPermissionDTO.toString())).build();
    }

    @GetMapping("static-permissions")
    public ResponseEntity staticPermissionList() {
        return ResponseEntity.ok(roleStaticPermissionService.staticPermissionList());
    }
}
