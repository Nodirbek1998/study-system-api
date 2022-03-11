package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.StudyLogs;
import uz.tatu.repository.StudyLogsRepository;
import uz.tatu.service.StudyLogsService;
import uz.tatu.service.dto.StudyLogsDTO;
import uz.tatu.service.mapper.StudyLogsMapper;

/**
 * Service Implementation for managing {@link StudyLogs}.
 */
@Service
@Transactional
public class StudyLogsServiceImpl implements StudyLogsService {

    private final Logger log = LoggerFactory.getLogger(StudyLogsServiceImpl.class);

    private final StudyLogsRepository studyLogsRepository;

    private final StudyLogsMapper studyLogsMapper;

    public StudyLogsServiceImpl(StudyLogsRepository studyLogsRepository, StudyLogsMapper studyLogsMapper) {
        this.studyLogsRepository = studyLogsRepository;
        this.studyLogsMapper = studyLogsMapper;
    }

    @Override
    public StudyLogsDTO save(StudyLogsDTO studyLogsDTO) {
        log.debug("Request to save StudyLogs : {}", studyLogsDTO);
        StudyLogs studyLogs = studyLogsMapper.toEntity(studyLogsDTO);
        studyLogs = studyLogsRepository.save(studyLogs);
        return studyLogsMapper.toDto(studyLogs);
    }

    @Override
    public Optional<StudyLogsDTO> partialUpdate(StudyLogsDTO studyLogsDTO) {
        log.debug("Request to partially update StudyLogs : {}", studyLogsDTO);

        return studyLogsRepository
            .findById(studyLogsDTO.getId())
            .map(existingStudyLogs -> {
                studyLogsMapper.partialUpdate(existingStudyLogs, studyLogsDTO);

                return existingStudyLogs;
            })
            .map(studyLogsRepository::save)
            .map(studyLogsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudyLogsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudyLogs");
        return studyLogsRepository.findAll(pageable).map(studyLogsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudyLogsDTO> findOne(Long id) {
        log.debug("Request to get StudyLogs : {}", id);
        return studyLogsRepository.findById(id).map(studyLogsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudyLogs : {}", id);
        studyLogsRepository.deleteById(id);
    }
}
