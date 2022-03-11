package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uz.tatu.domain.TestAnswer;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TestAnswerRepositoryWithBagRelationshipsImpl implements TestAnswerRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<TestAnswer> fetchBagRelationships(Optional<TestAnswer> testAnswer) {
        return testAnswer.map(this::fetchStudyUsers);
    }

    @Override
    public Page<TestAnswer> fetchBagRelationships(Page<TestAnswer> testAnswers) {
        return new PageImpl<>(fetchBagRelationships(testAnswers.getContent()), testAnswers.getPageable(), testAnswers.getTotalElements());
    }

    @Override
    public List<TestAnswer> fetchBagRelationships(List<TestAnswer> testAnswers) {
        return Optional.of(testAnswers).map(this::fetchStudyUsers).get();
    }

    TestAnswer fetchStudyUsers(TestAnswer result) {
        return entityManager
            .createQuery(
                "select testAnswer from TestAnswer testAnswer left join fetch testAnswer.studyUsers where testAnswer is :testAnswer",
                TestAnswer.class
            )
            .setParameter("testAnswer", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TestAnswer> fetchStudyUsers(List<TestAnswer> testAnswers) {
        return entityManager
            .createQuery(
                "select distinct testAnswer from TestAnswer testAnswer left join fetch testAnswer.studyUsers where testAnswer in :testAnswers",
                TestAnswer.class
            )
            .setParameter("testAnswers", testAnswers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
