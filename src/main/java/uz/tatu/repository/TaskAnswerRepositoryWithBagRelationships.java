package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import uz.tatu.domain.TaskAnswer;

public interface TaskAnswerRepositoryWithBagRelationships {
    Optional<TaskAnswer> fetchBagRelationships(Optional<TaskAnswer> taskAnswer);

    List<TaskAnswer> fetchBagRelationships(List<TaskAnswer> taskAnswers);

    Page<TaskAnswer> fetchBagRelationships(Page<TaskAnswer> taskAnswers);
}
