package uz.tatu.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.User;
import uz.tatu.domain.UserRole;
import uz.tatu.service.dto.UserRoleDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, RoleMapper.class})
public interface UserRoleMapper extends EntityMapper<UserRoleDto, UserRole>{

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "role.id", target = "roleId")
    UserRoleDto toDto(UserRole userRole);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "roleId", target = "role")
    UserRole toEntity(UserRoleDto userRoleDTO);


    default UserRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserRole userRole = new UserRole();
        userRole.setId(id);
        return userRole;
    }
}
