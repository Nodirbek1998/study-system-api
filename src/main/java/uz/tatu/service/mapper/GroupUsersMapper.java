package uz.tatu.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.GroupsUsers;
import uz.tatu.service.dto.GroupUsersDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, GroupsMapper.class})
public interface GroupUsersMapper extends EntityMapper<GroupUsersDto, GroupsUsers> {

    @Mapping(source = "users.id", target = "usersId")
    @Mapping(source = "groups.id", target = "groupsId")
    GroupUsersDto toDto(GroupsUsers s);

    @Mapping(source = "usersId", target = "users")
    @Mapping(source = "groupsId", target = "groups")
    GroupsUsers toEntity(GroupUsersDto s);

    default GroupsUsers fromId(Long id) {
        if (id == null) {
            return null;
        }
        GroupsUsers groupsUsers = new GroupsUsers();
        groupsUsers.setId(id);
        return groupsUsers;
    }
}
