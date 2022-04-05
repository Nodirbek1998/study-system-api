package uz.tatu.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.dto.RoleStaticPermissionDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.RoleStaticPermission}.
 */
public interface RoleStaticPermissionService {
    /**
     * Save a roleStaticPermission.
     *
     * @param roleStaticPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    RoleStaticPermissionDTO save(RoleStaticPermissionDTO roleStaticPermissionDTO);

    /**
     * Partially updates a roleStaticPermission.
     *
     * @param roleStaticPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoleStaticPermissionDTO> partialUpdate(RoleStaticPermissionDTO roleStaticPermissionDTO);

    /**
     * Get all the roleStaticPermissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoleStaticPermissionDTO> findAll(MultiValueMap<String, String> queryParams, Pageable pageable);

    /**
     * Get the "id" roleStaticPermission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoleStaticPermissionDTO> findOne(Long id);

    /**
     * Delete the "id" roleStaticPermission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Object staticPermissionList();

    void deleteByDTO(RoleStaticPermissionDTO roleStaticPermissionDTO);

    RoleStaticPermissionDTO saveAll(RoleStaticPermissionDTO roleStaticPermissionDTO);
}
