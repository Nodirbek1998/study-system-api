package uz.tatu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.dto.GroupsUsersDTO;

import java.util.Optional;

public interface GroupsUsersService {

    Page<GroupsUsersDTO> findAll(MultiValueMap<String, String> queryParams, Pageable pageable);

    Optional<GroupsUsersDTO> findOne(Long id);

    GroupsUsersDTO save(GroupsUsersDTO groupsUsersDTO);

    void delete(Long id);


}
