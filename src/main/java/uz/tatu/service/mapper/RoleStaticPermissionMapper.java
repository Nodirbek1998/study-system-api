package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.RoleStaticPermission;
import uz.tatu.service.dto.RoleStaticPermissionDTO;

/**
 * Mapper for the entity {@link RoleStaticPermission} and its DTO {@link RoleStaticPermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface RoleStaticPermissionMapper extends EntityMapper<RoleStaticPermissionDTO, RoleStaticPermission> {

    @Mapping(source = "roles.id", target = "roleId")
    RoleStaticPermissionDTO toDto(RoleStaticPermission s);

    @Mapping(source = "roleId", target = "roles")
    RoleStaticPermission toEntity(RoleStaticPermissionDTO edoDtRoleStaticPermissionDTO);

    default RoleStaticPermission fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoleStaticPermission edoDtRoleStaticPermission = new RoleStaticPermission();
        edoDtRoleStaticPermission.setId(id);
        return edoDtRoleStaticPermission;
    }
}
