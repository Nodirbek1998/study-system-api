package uz.tatu.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Groups;
import uz.tatu.domain.Subjects;
import uz.tatu.repository.SubjectsRepository;
import uz.tatu.repository.impl.SubjectsGroupsRepositoryImpl;
import uz.tatu.service.SubjectsService;
import uz.tatu.service.custom.GroupListDTO;
import uz.tatu.service.dto.SubjectsDTO;
import uz.tatu.service.mapper.GroupsMapper;
import uz.tatu.service.mapper.SubjectsMapper;
import uz.tatu.service.utils.RequestUtil;

/**
 * Service Implementation for managing {@link Subjects}.
 */
@Service
@Transactional
public class SubjectsServiceImpl implements SubjectsService {

    private final Logger log = LoggerFactory.getLogger(SubjectsServiceImpl.class);

    private final SubjectsRepository subjectsRepository;

    private final SubjectsMapper subjectsMapper;
    private final GroupsMapper groupsMapper;
    private final SubjectsGroupsRepositoryImpl subjectsGroupsRepositoryImpl;

    public SubjectsServiceImpl(SubjectsRepository subjectsRepository, SubjectsMapper subjectsMapper, GroupsMapper groupsMapper, SubjectsGroupsRepositoryImpl subjectsGroupsRepositoryImpl) {
        this.subjectsRepository = subjectsRepository;
        this.subjectsMapper = subjectsMapper;
        this.groupsMapper = groupsMapper;
        this.subjectsGroupsRepositoryImpl = subjectsGroupsRepositoryImpl;
    }

    @Override
    public SubjectsDTO save(SubjectsDTO subjectsDTO) {
        log.debug("Request to save Subjects : {}", subjectsDTO);
        Subjects subjects = subjectsMapper.toEntity(subjectsDTO);
        subjects = subjectsRepository.save(subjects);
        return subjectsMapper.toDto(subjects);
    }

    @Override
    public Optional<SubjectsDTO> partialUpdate(SubjectsDTO subjectsDTO) {
        log.debug("Request to partially update Subjects : {}", subjectsDTO);

        return subjectsRepository
            .findById(subjectsDTO.getId())
            .map(existingSubjects -> {
                subjectsMapper.partialUpdate(existingSubjects, subjectsDTO);

                return existingSubjects;
            })
            .map(subjectsRepository::save)
            .map(subjectsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectsDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParam) {
        log.debug("Request to get all Subjects");
        Page<Subjects> response = null;
        if (RequestUtil.checkValueNumber(queryParam, "groupId")){
            response = subjectsRepository.findByGroups_Id(Long.valueOf(queryParam.getFirst("groupId")), pageable);
        }else {
            response = subjectsRepository.findAll(pageable);
        }
        return response.map(subjectsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectsDTO> findOne(Long id) {
        Pageable pageable = PageRequest.of(0,100);
        log.debug("Request to get Subjects : {}", id);
        Optional<Subjects> byId = subjectsRepository.findById(id);
        Optional<SubjectsDTO> subjectsDTO = byId.map(subjectsMapper::toDto);
        if (byId.isPresent()){
            Page<GroupListDTO> allGroups = subjectsGroupsRepositoryImpl.findAllGroups(id, pageable);
           subjectsDTO.get().setGroupsDTOS(subjectsGroupsRepositoryImpl.findAllGroups(id, pageable).getContent());
        }
        return subjectsDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subjects : {}", id);
        subjectsRepository.deleteById(id);
    }
}
