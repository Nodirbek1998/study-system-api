package uz.tatu.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.RoleStaticPermission;
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.repository.RoleStaticPermissionRepository;
import uz.tatu.service.RoleStaticPermissionService;
import uz.tatu.service.dto.RoleStaticPermissionDTO;
import uz.tatu.service.mapper.RoleMapper;
import uz.tatu.service.mapper.RoleStaticPermissionMapper;
import uz.tatu.service.utils.RequestUtil;

/**
 * Service Implementation for managing {@link RoleStaticPermission}.
 */
@Service
@Transactional
public class RoleStaticPermissionServiceImpl implements RoleStaticPermissionService {

    private final Logger log = LoggerFactory.getLogger(RoleStaticPermissionServiceImpl.class);

    private final RoleStaticPermissionRepository roleStaticPermissionRepository;

    private final RoleStaticPermissionMapper roleStaticPermissionMapper;

    private final RoleMapper roleMapper;

    public RoleStaticPermissionServiceImpl(
            RoleStaticPermissionRepository roleStaticPermissionRepository,
            RoleStaticPermissionMapper roleStaticPermissionMapper,
            RoleMapper roleMapper) {
        this.roleStaticPermissionRepository = roleStaticPermissionRepository;
        this.roleStaticPermissionMapper = roleStaticPermissionMapper;
        this.roleMapper = roleMapper;
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
    public Page<RoleStaticPermissionDTO> findAll(MultiValueMap<String, String> queryParams, Pageable pageable) {
        log.debug("Request to get all EdoDtRoleStaticPermissions");
        Long roleId = 0L;
        if(RequestUtil.checkValueNumber(queryParams, "roleId")){
            roleId = Long.valueOf(queryParams.getFirst("roleId"));
        }

        if(roleId > 0L){
            return roleStaticPermissionRepository.findByRoles_Id(roleId, pageable)
                    .map(roleStaticPermissionMapper::toDto);
        }
        return roleStaticPermissionRepository.findAll(pageable)
                .map(roleStaticPermissionMapper::toDto);
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

    @Override
    public Object staticPermissionList() {
        List<EnumStaticPermission> values = Arrays.asList(EnumStaticPermission.values());
        return values;
    }

    @Override
    public void deleteByDTO(RoleStaticPermissionDTO roleStaticPermissionDTO) {
        log.debug("Request to delete RoleStaticPermission : {}", roleStaticPermissionDTO);
        if(roleStaticPermissionDTO.getStaticPermissions() == null){
            roleStaticPermissionRepository.deleteByRoles_IdAndStaticPermission(roleStaticPermissionDTO.getRoleId(), roleStaticPermissionDTO.getStaticPermission());
        } else {
            roleStaticPermissionDTO.getStaticPermissions().forEach(permission->{
                roleStaticPermissionRepository.deleteByRoles_IdAndStaticPermission(roleStaticPermissionDTO.getRoleId(), permission);
            });
        }
    }

    @Override
    public RoleStaticPermissionDTO saveAll(RoleStaticPermissionDTO roleStaticPermissionDTO) {
        log.debug("Request to save EdoDtRoleStaticPermission : {}", roleStaticPermissionDTO);
        if(roleStaticPermissionDTO.getStaticPermissions() == null){
            return roleStaticPermissionDTO;
        }
        Long roleId = roleStaticPermissionDTO.getRoleId();
        roleStaticPermissionDTO.getStaticPermissions().forEach(permission->{
            Optional<RoleStaticPermission> roleStaticPermission = roleStaticPermissionRepository.findFirstByRoles_IdAndStaticPermission(roleId, permission);

            RoleStaticPermission roleStaticPermission1 = new RoleStaticPermission();
            roleStaticPermission.ifPresent(dtRoleStaticPermission -> roleStaticPermission1.setId(dtRoleStaticPermission.getId()));
            roleStaticPermission1.setStaticPermission(permission);
            roleStaticPermission1.setRoles(roleMapper.fromId(roleId));

            roleStaticPermissionRepository.save(roleStaticPermission1);

            roleStaticPermissionDTO.setId(roleStaticPermission1.getId());
        });

        return roleStaticPermissionDTO;
    }
}
