package uz.tatu.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.TestQuestion;

/**
 * Spring Data SQL repository for the TestQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {}
