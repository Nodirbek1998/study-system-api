package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.Tests;
import uz.tatu.repository.TestsRepository;
import uz.tatu.service.TestsService;
import uz.tatu.service.dto.TestsDTO;
import uz.tatu.service.mapper.TestsMapper;

/**
 * Service Implementation for managing {@link Tests}.
 */
@Service
@Transactional
public class TestsServiceImpl implements TestsService {

    private final Logger log = LoggerFactory.getLogger(TestsServiceImpl.class);

    private final TestsRepository testsRepository;

    private final TestsMapper testsMapper;

    public TestsServiceImpl(TestsRepository testsRepository, TestsMapper testsMapper) {
        this.testsRepository = testsRepository;
        this.testsMapper = testsMapper;
    }

    @Override
    public TestsDTO save(TestsDTO testsDTO) {
        log.debug("Request to save Tests : {}", testsDTO);
        Tests tests = testsMapper.toEntity(testsDTO);
        tests = testsRepository.save(tests);
        return testsMapper.toDto(tests);
    }

    @Override
    public Optional<TestsDTO> partialUpdate(TestsDTO testsDTO) {
        log.debug("Request to partially update Tests : {}", testsDTO);

        return testsRepository
            .findById(testsDTO.getId())
            .map(existingTests -> {
                testsMapper.partialUpdate(existingTests, testsDTO);

                return existingTests;
            })
            .map(testsRepository::save)
            .map(testsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tests");
        return testsRepository.findAll(pageable).map(testsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestsDTO> findOne(Long id) {
        log.debug("Request to get Tests : {}", id);
        return testsRepository.findById(id).map(testsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tests : {}", id);
        testsRepository.deleteById(id);
    }
}
