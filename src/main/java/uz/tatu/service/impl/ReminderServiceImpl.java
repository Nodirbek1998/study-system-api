package uz.tatu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Reminder;
import uz.tatu.repository.ReminderRepository;
import uz.tatu.service.ReminderService;
import uz.tatu.service.dto.ReminderDTO;
import uz.tatu.service.mapper.ReminderMapper;

import java.util.Optional;

@Service
@Transactional
public class ReminderServiceImpl implements ReminderService {

    private final Logger log = LoggerFactory.getLogger(ReminderServiceImpl.class);


    private final ReminderMapper reminderMapper;

    private final ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderMapper reminderMapper, ReminderRepository reminderRepository) {
        this.reminderMapper = reminderMapper;
        this.reminderRepository = reminderRepository;
    }


    @Override
    public ReminderDTO save(ReminderDTO reminderDTO) {
        log.debug("Request to save Reminder : {}", reminderDTO);
        Reminder reminder = reminderRepository.save(reminderMapper.toEntity(reminderDTO));
        return reminderMapper.toDto(reminder);
    }

    @Override
    public Optional<ReminderDTO> partialUpdate(ReminderDTO reminderDTO) {
        log.debug("Request to partially update Reminder : {}", reminderDTO);
        return reminderRepository
                .findById(reminderDTO.getId())
                .map(existingTask -> {
                    reminderMapper.partialUpdate(existingTask, reminderDTO);

                    return existingTask;
                })
                .map(reminderRepository::save)
                .map(reminderMapper::toDto);
    }

    @Override
    public Page<ReminderDTO> findAll(MultiValueMap<String, String> queryParam, Pageable pageable) {
        log.debug("Request to get all Reminder");
        return reminderRepository.findAll(pageable).map(reminderMapper::toDto);
    }

    @Override
    public Optional<ReminderDTO> findOne(Long id) {
        log.debug("Request to get Reminder : {}", id);
        return reminderRepository.findById(id).map(reminderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reminder : {}", id);
        reminderRepository.deleteById(id);
    }
}
