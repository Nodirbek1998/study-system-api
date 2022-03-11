package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.TestAnswer;
import uz.tatu.repository.TestAnswerRepository;
import uz.tatu.service.TestAnswerService;
import uz.tatu.service.dto.TestAnswerDTO;
import uz.tatu.service.mapper.TestAnswerMapper;

/**
 * Service Implementation for managing {@link TestAnswer}.
 */
@Service
@Transactional
public class TestAnswerServiceImpl implements TestAnswerService {

    private final Logger log = LoggerFactory.getLogger(TestAnswerServiceImpl.class);

    private final TestAnswerRepository testAnswerRepository;

    private final TestAnswerMapper testAnswerMapper;

    public TestAnswerServiceImpl(TestAnswerRepository testAnswerRepository, TestAnswerMapper testAnswerMapper) {
        this.testAnswerRepository = testAnswerRepository;
        this.testAnswerMapper = testAnswerMapper;
    }

    @Override
    public TestAnswerDTO save(TestAnswerDTO testAnswerDTO) {
        log.debug("Request to save TestAnswer : {}", testAnswerDTO);
        TestAnswer testAnswer = testAnswerMapper.toEntity(testAnswerDTO);
        testAnswer = testAnswerRepository.save(testAnswer);
        return testAnswerMapper.toDto(testAnswer);
    }

    @Override
    public Optional<TestAnswerDTO> partialUpdate(TestAnswerDTO testAnswerDTO) {
        log.debug("Request to partially update TestAnswer : {}", testAnswerDTO);

        return testAnswerRepository
            .findById(testAnswerDTO.getId())
            .map(existingTestAnswer -> {
                testAnswerMapper.partialUpdate(existingTestAnswer, testAnswerDTO);

                return existingTestAnswer;
            })
            .map(testAnswerRepository::save)
            .map(testAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestAnswers");
        return testAnswerRepository.findAll(pageable).map(testAnswerMapper::toDto);
    }

    public Page<TestAnswerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return testAnswerRepository.findAllWithEagerRelationships(pageable).map(testAnswerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestAnswerDTO> findOne(Long id) {
        log.debug("Request to get TestAnswer : {}", id);
        return testAnswerRepository.findOneWithEagerRelationships(id).map(testAnswerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestAnswer : {}", id);
        testAnswerRepository.deleteById(id);
    }
}
