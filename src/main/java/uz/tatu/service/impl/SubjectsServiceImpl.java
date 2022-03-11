package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.Subjects;
import uz.tatu.repository.SubjectsRepository;
import uz.tatu.service.SubjectsService;
import uz.tatu.service.dto.SubjectsDTO;
import uz.tatu.service.mapper.SubjectsMapper;

/**
 * Service Implementation for managing {@link Subjects}.
 */
@Service
@Transactional
public class SubjectsServiceImpl implements SubjectsService {

    private final Logger log = LoggerFactory.getLogger(SubjectsServiceImpl.class);

    private final SubjectsRepository subjectsRepository;

    private final SubjectsMapper subjectsMapper;

    public SubjectsServiceImpl(SubjectsRepository subjectsRepository, SubjectsMapper subjectsMapper) {
        this.subjectsRepository = subjectsRepository;
        this.subjectsMapper = subjectsMapper;
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
    public Page<SubjectsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subjects");
        return subjectsRepository.findAll(pageable).map(subjectsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectsDTO> findOne(Long id) {
        log.debug("Request to get Subjects : {}", id);
        return subjectsRepository.findById(id).map(subjectsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subjects : {}", id);
        subjectsRepository.deleteById(id);
    }
}
