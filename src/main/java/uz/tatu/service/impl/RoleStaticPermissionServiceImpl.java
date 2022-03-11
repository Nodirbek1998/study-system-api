package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.RoleStaticPermission;
import uz.tatu.repository.RoleStaticPermissionRepository;
import uz.tatu.service.RoleStaticPermissionService;
import uz.tatu.service.dto.RoleStaticPermissionDTO;
import uz.tatu.service.mapper.RoleStaticPermissionMapper;

/**
 * Service Implementation for managing {@link RoleStaticPermission}.
 */
@Service
@Transactional
public class RoleStaticPermissionServiceImpl implements RoleStaticPermissionService {

    private final Logger log = LoggerFactory.getLogger(RoleStaticPermissionServiceImpl.class);

    private final RoleStaticPermissionRepository roleStaticPermissionRepository;

    private final RoleStaticPermissionMapper roleStaticPermissionMapper;

    public RoleStaticPermissionServiceImpl(
        RoleStaticPermissionRepository roleStaticPermissionRepository,
        RoleStaticPermissionMapper roleStaticPermissionMapper
    ) {
        this.roleStaticPermissionRepository = roleStaticPermissionRepository;
        this.roleStaticPermissionMapper = roleStaticPermissionMapper;
    }

    @Override
    public RoleStaticPermissionDTO save(RoleStaticPermissionDTO roleStaticPermissionDTO) {
        log.debug("Request to save RoleStaticPermission : {}", roleStaticPermissionDTO);
        RoleStaticPermission roleStaticPermission = roleStaticPermissionMapper.toEntity(roleStaticPermissionDTO);
        roleStaticPermission = roleStaticPermissionRepository.save(roleStaticPermission);
        return roleStaticPermissionMapper.toDto(roleStaticPermission);
    }

    @Override
    public Optional<RoleStaticPermissionDTO> partialUpdate(RoleStaticPermissionDTO roleStaticPermissionDTO) {
        log.debug("Request to partially update RoleStaticPermission : {}", roleStaticPermissionDTO);

        return roleStaticPermissionRepository
            .findById(roleStaticPermissionDTO.getId())
            .map(existingRoleStaticPermission -> {
                roleStaticPermissionMapper.partialUpdate(existingRoleStaticPermission, roleStaticPermissionDTO);

                return existingRoleStaticPermission;
            })
            .map(roleStaticPermissionRepository::save)
            .map(roleStaticPermissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleStaticPermissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoleStaticPermissions");
        return roleStaticPermissionRepository.findAll(pageable).map(roleStaticPermissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleStaticPermissionDTO> findOne(Long id) {
        log.debug("Request to get RoleStaticPermission : {}", id);
        return roleStaticPermissionRepository.findById(id).map(roleStaticPermissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoleStaticPermission : {}", id);
        roleStaticPermissionRepository.deleteById(id);
    }
}
