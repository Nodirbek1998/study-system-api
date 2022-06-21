package uz.tatu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Examples;
import uz.tatu.domain.Files;
import uz.tatu.repository.ExamplesRepository;
import uz.tatu.repository.FilesRepository;
import uz.tatu.repository.impl.ExamplesRepositoryImpl;
import uz.tatu.service.ExamplesService;
import uz.tatu.service.custom.ExamplesAnswerListDTO;
import uz.tatu.service.custom.ExamplesListDTO;
import uz.tatu.service.dto.ExamplesDTO;
import uz.tatu.service.mapper.ExamplesMapper;
import uz.tatu.service.mapper.FilesMapper;

import java.util.Optional;

@Service
public class ExamplesServiceImpl implements ExamplesService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ExamplesMapper examplesMapper;

    private final ExamplesRepository examplesRepository;

    private final FilesRepository filesRepository;

    private final FilesMapper filesMapper;

    private final ExamplesRepositoryImpl examplesRepositoryImpl;

    public ExamplesServiceImpl(ExamplesMapper examplesMapper, ExamplesRepository examplesRepository, FilesRepository filesRepository, FilesMapper filesMapper, ExamplesRepositoryImpl examplesRepositoryImpl) {
        this.examplesMapper = examplesMapper;
        this.examplesRepository = examplesRepository;
        this.filesRepository = filesRepository;
        this.filesMapper = filesMapper;
        this.examplesRepositoryImpl = examplesRepositoryImpl;
    }


    @Override
    public ExamplesDTO save(ExamplesDTO examplesDTO) {
        log.debug("Request to save Examples : {}", examplesDTO);
        Files files = new Files();
        Examples examples = examplesMapper.toEntity(examplesDTO);
        if (examplesDTO.getFilesDTO().getId() != null){
            files = filesRepository.save(filesMapper.toEntity(examplesDTO.getFilesDTO()));
        }
        examples.setFiles(files);
        examples = examplesRepository.save(examples);
        examples.setFiles(files);
        return examplesMapper.toDto(examples);
    }

    @Override
    public Page<ExamplesListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams) {
        log.debug("Request to get all ExamplesAnswers");
        Page<ExamplesListDTO> all = examplesRepositoryImpl.findAll(queryParams, pageable);
        return all;
    }

    @Override
    public Page<ExamplesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ExamplesDTO> findOne(Long id) {
        log.debug("Request to get Examples : {}", id);
        Optional<Examples> examples = examplesRepository.findById(id);
        Optional<ExamplesDTO> examplesDTO = examples.map(examplesMapper::toDto);
        if (examples.isPresent() && examples.isPresent() && examples.get().getFiles() != null){
            examplesDTO.get().setFilesDTO(filesMapper.toDto(examples.get().getFiles()));
        }
        return examplesDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Examples : {}", id);
        examplesRepository.deleteById(id);
    }
}
