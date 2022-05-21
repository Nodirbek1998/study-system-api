package uz.tatu.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.GroupsUsers;
import uz.tatu.repository.GroupsUsersRepository;
import uz.tatu.service.GroupsUsersService;
import uz.tatu.service.dto.GroupsUsersDTO;
import uz.tatu.service.mapper.GroupsUsersMapper;
import uz.tatu.service.utils.RequestUtil;

import java.util.Optional;

@Service
public class GroupsUsersServiceImpl implements GroupsUsersService {

    private final GroupsUsersRepository groupsUsersRepository;
    private final GroupsUsersMapper groupsUsersMapper;

    public GroupsUsersServiceImpl(GroupsUsersRepository groupsUsersRepository, GroupsUsersMapper groupsUsersMapper) {
        this.groupsUsersRepository = groupsUsersRepository;
        this.groupsUsersMapper = groupsUsersMapper;
    }

    @Override
    public Page<GroupsUsersDTO> findAll(MultiValueMap<String, String> queryParams, Pageable pageable) {
        Long userId = 0L;
        Long groupId = 0L;
        if (RequestUtil.checkValueNumber(queryParams, "userId")){
            userId = Long.valueOf(queryParams.getFirst("userId"));
        }
        if (RequestUtil.checkValueNumber(queryParams, "groupId")){
            groupId = Long.valueOf(queryParams.getFirst("groupId"));
        }

        Page<GroupsUsers> response =null;

        if (groupId != 0 ){
           response = groupsUsersRepository.findByGroups_Id(groupId, pageable);
        }
        if (userId != 0 ){
           response = groupsUsersRepository.findByGroups_Id(userId, pageable);
        }

        return response.map(groupsUsersMapper::toDto);
    }

    @Override
    public Optional<GroupsUsersDTO> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public GroupsUsersDTO save(GroupsUsersDTO groupsUsersDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
