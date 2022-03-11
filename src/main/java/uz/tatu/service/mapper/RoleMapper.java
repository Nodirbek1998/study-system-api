package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Role;
import uz.tatu.service.dto.RoleDTO;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoId(Role role);
}
