package uz.tatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tatu.domain.Reminder;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
