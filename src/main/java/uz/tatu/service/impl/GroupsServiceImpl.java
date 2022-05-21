package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Groups;
import uz.tatu.repository.GroupsRepository;
import uz.tatu.repository.SubjectsRepository;
import uz.tatu.service.GroupsService;
import uz.tatu.service.GroupsUsersService;
import uz.tatu.service.SubjectsService;
import uz.tatu.service.dto.GroupsDTO;
import uz.tatu.service.mapper.GroupsMapper;

/**
 * Service Implementation for managing {@link Groups}.
 */
@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsRepository groupsRepository;

    private final GroupsMapper groupsMapper;

    private final GroupsUsersService groupsUsersService;

    private final SubjectsService subjectsService;

    public GroupsServiceImpl(GroupsRepository groupsRepository, GroupsMapper groupsMapper, GroupsUsersService groupsUsersService, SubjectsService subjectsService) {
        this.groupsRepository = groupsRepository;
        this.groupsMapper = groupsMapper;
        this.groupsUsersService = groupsUsersService;
        this.subjectsService = subjectsService;
    }

    @Override
    public GroupsDTO save(GroupsDTO groupsDTO) {
        log.debug("Request to save Groups : {}", groupsDTO);
        Groups groups = groupsMapper.toEntity(groupsDTO);
        groups = groupsRepository.save(groups);
        return groupsMapper.toDto(groups);
    }

    @Override
    public Optional<GroupsDTO> partialUpdate(GroupsDTO groupsDTO) {
        log.debug("Request to partially update Groups : {}", groupsDTO);

        return groupsRepository
            .findById(groupsDTO.getId())
            .map(existingGroups -> {
                groupsMapper.partialUpdate(existingGroups, groupsDTO);

                return existingGroups;
            })
            .map(groupsRepository::save)
            .map(groupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll(pageable).map(groupsMapper::toDto);
    }

    public Page<GroupsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return groupsRepository.findAllWithEagerRelationships(pageable).map(groupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroupsDTO> findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
        Pageable pageable =  PageRequest.of(0, 100);
        Optional<GroupsDTO> groupsDTO = groupsRepository.findById(id).map(groupsMapper::toDto);
        if (groupsDTO.isPresent()){
            queryParam.set("groupId", id + "");
            groupsDTO.get().setGroupsUsersList(groupsUsersService.findAll(queryParam, pageable).getContent());
            groupsDTO.get().setSubjects(subjectsService.findAll(pageable, queryParam).getContent());
        }
        return groupsDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.deleteById(id);
    }
}
