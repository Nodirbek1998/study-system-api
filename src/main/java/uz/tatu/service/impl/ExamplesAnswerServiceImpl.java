package uz.tatu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.ExamplesAnswer;
import uz.tatu.domain.Files;
import uz.tatu.repository.ExamplesAnswerRepository;
import uz.tatu.repository.FilesRepository;
import uz.tatu.repository.impl.ExamplesAnswerRepositoryImpl;
import uz.tatu.service.ExamplesAnswerService;
import uz.tatu.service.custom.ExamplesAnswerListDTO;
import uz.tatu.service.dto.ExamplesAnswerDTO;
import uz.tatu.service.mapper.ExamplesAnswerMapper;
import uz.tatu.service.mapper.FilesMapper;

import java.util.Optional;

@Service
public class ExamplesAnswerServiceImpl implements ExamplesAnswerService {

    private final Logger log = LoggerFactory.getLogger(ExamplesAnswerServiceImpl.class);


    private final FilesRepository filesRepository;

    private final FilesMapper filesMapper;

    private final ExamplesAnswerMapper examplesAnswerMapper;

    private final ExamplesAnswerRepository examplesAnswerRepository;

    private final ExamplesAnswerRepositoryImpl examplesAnswerRepositoryImpl;

    public ExamplesAnswerServiceImpl(FilesRepository filesRepository, FilesMapper filesMapper, ExamplesAnswerMapper examplesAnswerMapper, ExamplesAnswerRepository examplesAnswerRepository, ExamplesAnswerRepositoryImpl examplesAnswerRepositoryImpl) {
        this.filesRepository = filesRepository;
        this.filesMapper = filesMapper;
        this.examplesAnswerMapper = examplesAnswerMapper;
        this.examplesAnswerRepository = examplesAnswerRepository;
        this.examplesAnswerRepositoryImpl = examplesAnswerRepositoryImpl;
    }

    @Override
    public ExamplesAnswerDTO save(ExamplesAnswerDTO examplesAnswerDTO) {
        log.debug("Request to save ExamplesAnswer : {}", examplesAnswerDTO);
        Files files = new Files();
        ExamplesAnswer examplesAnswer = examplesAnswerMapper.toEntity(examplesAnswerDTO);
        if (examplesAnswerDTO.getFilesDTO().getId() != null){
            files = filesRepository.save(filesMapper.toEntity(examplesAnswerDTO.getFilesDTO()));
        }
        examplesAnswer.setFiles(files);
        examplesAnswer = examplesAnswerRepository.save(examplesAnswer);
        examplesAnswer.setFiles(files);
        return examplesAnswerMapper.toDto(examplesAnswer);
    }

    @Override
    public Page<ExamplesAnswerListDTO> findAll(Pageable pageable, MultiValueMap<String, String> queryParams) {
        log.debug("Request to get all ExamplesAnswers");
        Page<ExamplesAnswerListDTO> all = examplesAnswerRepositoryImpl.findAll(queryParams, pageable);
        return all;
    }

    @Override
    public Page<ExamplesAnswerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ExamplesAnswerDTO> findOne(Long id) {
        log.debug("Request to get ExamplesAnswer : {}", id);
        Optional<ExamplesAnswer> examplesAnswer = examplesAnswerRepository.findById(id);
        Optional<ExamplesAnswerDTO> examplesAnswerDTO = examplesAnswer.map(examplesAnswerMapper::toDto);
        if (examplesAnswer.isPresent() && examplesAnswer.isPresent() && examplesAnswer.get().getFiles() != null){
            examplesAnswerDTO.get().setFilesDTO(filesMapper.toDto(examplesAnswer.get().getFiles()));
        }
        return examplesAnswerDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamplesAnswer : {}", id);
        examplesAnswerRepository.deleteById(id);
    }
}
