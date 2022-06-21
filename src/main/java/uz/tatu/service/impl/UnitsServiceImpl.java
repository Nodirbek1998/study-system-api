package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Units;
import uz.tatu.repository.UnitsRepository;
import uz.tatu.repository.impl.UnitsRepositoryImpl;
import uz.tatu.service.UnitsService;
import uz.tatu.service.custom.UnitsListDTO;
import uz.tatu.service.dto.UnitsDTO;
import uz.tatu.service.mapper.UnitsMapper;

/**
 * Service Implementation for managing {@link Units}.
 */
@Service
@Transactional
public class UnitsServiceImpl implements UnitsService {

    private final Logger log = LoggerFactory.getLogger(UnitsServiceImpl.class);

    private final UnitsRepository unitsRepository;

    private final UnitsRepositoryImpl unitsRepositoryImpl;

    private final UnitsMapper unitsMapper;

    public UnitsServiceImpl(UnitsRepository unitsRepository, UnitsRepositoryImpl unitsRepositoryImpl, UnitsMapper unitsMapper) {
        this.unitsRepository = unitsRepository;
        this.unitsRepositoryImpl = unitsRepositoryImpl;
        this.unitsMapper = unitsMapper;
    }

    @Override
    public UnitsDTO save(UnitsDTO unitsDTO) {
        log.debug("Request to save Units : {}", unitsDTO);
        Units units = unitsMapper.toEntity(unitsDTO);
        units = unitsRepository.save(units);
        return unitsMapper.toDto(units);
    }

    @Override
    public Optional<UnitsDTO> partialUpdate(UnitsDTO unitsDTO) {
        log.debug("Request to partially update Units : {}", unitsDTO);

        return unitsRepository
            .findById(unitsDTO.getId())
            .map(existingUnits -> {
                unitsMapper.partialUpdate(existingUnits, unitsDTO);

                return existingUnits;
            })
            .map(unitsRepository::save)
            .map(unitsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitsListDTO> findAll(Pageable pageable, MultiValueMap<String , String> queryParam) {
        log.debug("Request to get all Units");
        return unitsRepositoryImpl.findAll(pageable, queryParam);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitsDTO> findOne(Long id) {
        log.debug("Request to get Units : {}", id);
        return unitsRepository.findById(id).map(unitsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Units : {}", id);
        unitsRepository.deleteById(id);
    }
}
