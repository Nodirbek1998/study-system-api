package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.RoleStaticPermission;
import uz.tatu.service.dto.RoleStaticPermissionDTO;

/**
 * Mapper for the entity {@link RoleStaticPermission} and its DTO {@link RoleStaticPermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface RoleStaticPermissionMapper extends EntityMapper<RoleStaticPermissionDTO, RoleStaticPermission> {
    @Mapping(target = "role", source = "role", qualifiedByName = "id")
    RoleStaticPermissionDTO toDto(RoleStaticPermission s);
}
