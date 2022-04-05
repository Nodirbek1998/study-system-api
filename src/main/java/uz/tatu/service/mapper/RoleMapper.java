package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Role;
import uz.tatu.service.dto.RoleDTO;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {


    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role edoDtRoles = new Role();
        edoDtRoles.setId(id);
        return edoDtRoles;
    }
}
