package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.Files;
import uz.tatu.repository.FilesRepository;
import uz.tatu.service.FilesService;
import uz.tatu.service.dto.FilesDTO;
import uz.tatu.service.mapper.FilesMapper;

/**
 * Service Implementation for managing {@link Files}.
 */
@Service
@Transactional
public class FilesServiceImpl implements FilesService {

    private final Logger log = LoggerFactory.getLogger(FilesServiceImpl.class);

    private final FilesRepository filesRepository;

    private final FilesMapper filesMapper;

    public FilesServiceImpl(FilesRepository filesRepository, FilesMapper filesMapper) {
        this.filesRepository = filesRepository;
        this.filesMapper = filesMapper;
    }

    @Override
    public FilesDTO save(FilesDTO filesDTO) {
        log.debug("Request to save Files : {}", filesDTO);
        Files files = filesMapper.toEntity(filesDTO);
        files = filesRepository.save(files);
        return filesMapper.toDto(files);
    }

    @Override
    public Optional<FilesDTO> partialUpdate(FilesDTO filesDTO) {
        log.debug("Request to partially update Files : {}", filesDTO);

        return filesRepository
            .findById(filesDTO.getId())
            .map(existingFiles -> {
                filesMapper.partialUpdate(existingFiles, filesDTO);

                return existingFiles;
            })
            .map(filesRepository::save)
            .map(filesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return filesRepository.findAll(pageable).map(filesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FilesDTO> findOne(Long id) {
        log.debug("Request to get Files : {}", id);
        return filesRepository.findById(id).map(filesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Files : {}", id);
        filesRepository.deleteById(id);
    }
}
