package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.StudyUsers;
import uz.tatu.repository.StudyUsersRepository;
import uz.tatu.service.StudyUsersService;
import uz.tatu.service.dto.StudyUsersDTO;
import uz.tatu.service.mapper.StudyUsersMapper;

/**
 * Service Implementation for managing {@link StudyUsers}.
 */
@Service
@Transactional
public class StudyUsersServiceImpl implements StudyUsersService {

    private final Logger log = LoggerFactory.getLogger(StudyUsersServiceImpl.class);

    private final StudyUsersRepository studyUsersRepository;

    private final StudyUsersMapper studyUsersMapper;

    public StudyUsersServiceImpl(StudyUsersRepository studyUsersRepository, StudyUsersMapper studyUsersMapper) {
        this.studyUsersRepository = studyUsersRepository;
        this.studyUsersMapper = studyUsersMapper;
    }

    @Override
    public StudyUsersDTO save(StudyUsersDTO studyUsersDTO) {
        log.debug("Request to save StudyUsers : {}", studyUsersDTO);
        StudyUsers studyUsers = studyUsersMapper.toEntity(studyUsersDTO);
        studyUsers = studyUsersRepository.save(studyUsers);
        return studyUsersMapper.toDto(studyUsers);
    }

    @Override
    public Optional<StudyUsersDTO> partialUpdate(StudyUsersDTO studyUsersDTO) {
        log.debug("Request to partially update StudyUsers : {}", studyUsersDTO);

        return studyUsersRepository
            .findById(studyUsersDTO.getId())
            .map(existingStudyUsers -> {
                studyUsersMapper.partialUpdate(existingStudyUsers, studyUsersDTO);

                return existingStudyUsers;
            })
            .map(studyUsersRepository::save)
            .map(studyUsersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudyUsersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudyUsers");
        return studyUsersRepository.findAll(pageable).map(studyUsersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudyUsersDTO> findOne(Long id) {
        log.debug("Request to get StudyUsers : {}", id);
        return studyUsersRepository.findById(id).map(studyUsersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudyUsers : {}", id);
        studyUsersRepository.deleteById(id);
    }
}
