package uz.tatu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.tatu.service.dto.ReminderDTO;

import java.util.Optional;

public interface ReminderService {

    /**
     * Save a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    ReminderDTO save(ReminderDTO reminderDTO);

    /**
     * Partially updates a reminder.
     *
     * @param reminderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReminderDTO> partialUpdate(ReminderDTO reminderDTO);

    /**
     * Get all the reminders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReminderDTO> findAll(MultiValueMap<String, String > queryParam, Pageable pageable);

    /**
     * Get the "id" reminder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReminderDTO> findOne(Long id);

    /**
     * Delete the "id" reminder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
