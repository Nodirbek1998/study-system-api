package uz.tatu.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.StudyLogs;

/**
 * Spring Data SQL repository for the StudyLogs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyLogsRepository extends JpaRepository<StudyLogs, Long> {}
