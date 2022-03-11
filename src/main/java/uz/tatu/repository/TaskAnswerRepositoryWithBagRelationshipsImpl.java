package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uz.tatu.domain.TaskAnswer;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TaskAnswerRepositoryWithBagRelationshipsImpl implements TaskAnswerRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<TaskAnswer> fetchBagRelationships(Optional<TaskAnswer> taskAnswer) {
        return taskAnswer.map(this::fetchStudyUsers);
    }

    @Override
    public Page<TaskAnswer> fetchBagRelationships(Page<TaskAnswer> taskAnswers) {
        return new PageImpl<>(fetchBagRelationships(taskAnswers.getContent()), taskAnswers.getPageable(), taskAnswers.getTotalElements());
    }

    @Override
    public List<TaskAnswer> fetchBagRelationships(List<TaskAnswer> taskAnswers) {
        return Optional.of(taskAnswers).map(this::fetchStudyUsers).get();
    }

    TaskAnswer fetchStudyUsers(TaskAnswer result) {
        return entityManager
            .createQuery(
                "select taskAnswer from TaskAnswer taskAnswer left join fetch taskAnswer.studyUsers where taskAnswer is :taskAnswer",
                TaskAnswer.class
            )
            .setParameter("taskAnswer", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TaskAnswer> fetchStudyUsers(List<TaskAnswer> taskAnswers) {
        return entityManager
            .createQuery(
                "select distinct taskAnswer from TaskAnswer taskAnswer left join fetch taskAnswer.studyUsers where taskAnswer in :taskAnswers",
                TaskAnswer.class
            )
            .setParameter("taskAnswers", taskAnswers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
