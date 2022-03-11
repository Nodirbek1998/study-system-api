package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.TestQuestion;
import uz.tatu.repository.TestQuestionRepository;
import uz.tatu.service.TestQuestionService;
import uz.tatu.service.dto.TestQuestionDTO;
import uz.tatu.service.mapper.TestQuestionMapper;

/**
 * Service Implementation for managing {@link TestQuestion}.
 */
@Service
@Transactional
public class TestQuestionServiceImpl implements TestQuestionService {

    private final Logger log = LoggerFactory.getLogger(TestQuestionServiceImpl.class);

    private final TestQuestionRepository testQuestionRepository;

    private final TestQuestionMapper testQuestionMapper;

    public TestQuestionServiceImpl(TestQuestionRepository testQuestionRepository, TestQuestionMapper testQuestionMapper) {
        this.testQuestionRepository = testQuestionRepository;
        this.testQuestionMapper = testQuestionMapper;
    }

    @Override
    public TestQuestionDTO save(TestQuestionDTO testQuestionDTO) {
        log.debug("Request to save TestQuestion : {}", testQuestionDTO);
        TestQuestion testQuestion = testQuestionMapper.toEntity(testQuestionDTO);
        testQuestion = testQuestionRepository.save(testQuestion);
        return testQuestionMapper.toDto(testQuestion);
    }

    @Override
    public Optional<TestQuestionDTO> partialUpdate(TestQuestionDTO testQuestionDTO) {
        log.debug("Request to partially update TestQuestion : {}", testQuestionDTO);

        return testQuestionRepository
            .findById(testQuestionDTO.getId())
            .map(existingTestQuestion -> {
                testQuestionMapper.partialUpdate(existingTestQuestion, testQuestionDTO);

                return existingTestQuestion;
            })
            .map(testQuestionRepository::save)
            .map(testQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestQuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TestQuestions");
        return testQuestionRepository.findAll(pageable).map(testQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestQuestionDTO> findOne(Long id) {
        log.debug("Request to get TestQuestion : {}", id);
        return testQuestionRepository.findById(id).map(testQuestionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestQuestion : {}", id);
        testQuestionRepository.deleteById(id);
    }
}
