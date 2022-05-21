package uz.tatu.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.GroupsUsers;
import uz.tatu.service.dto.GroupsUsersDTO;

@Mapper(componentModel = "spring", uses = {UserMapper.class, GroupsMapper.class})
public interface GroupsUsersMapper extends EntityMapper<GroupsUsersDTO, GroupsUsers>{


    @Mapping(source = "users.id", target = "userId")
    @Mapping(source = "users.firstName", target = "firstName")
    @Mapping(source = "users.lastName", target = "lastName")
    @Mapping(source = "groups.name", target = "groupName")
    @Mapping(source = "groups.id", target = "groupId")
    GroupsUsersDTO toDto(GroupsUsers groupsUsers);
}
