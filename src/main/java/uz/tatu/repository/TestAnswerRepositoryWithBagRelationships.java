package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import uz.tatu.domain.TestAnswer;

public interface TestAnswerRepositoryWithBagRelationships {
    Optional<TestAnswer> fetchBagRelationships(Optional<TestAnswer> testAnswer);

    List<TestAnswer> fetchBagRelationships(List<TestAnswer> testAnswers);

    Page<TestAnswer> fetchBagRelationships(Page<TestAnswer> testAnswers);
}
