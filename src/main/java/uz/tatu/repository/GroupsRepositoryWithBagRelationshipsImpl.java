package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uz.tatu.domain.Groups;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class GroupsRepositoryWithBagRelationshipsImpl implements GroupsRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Groups> fetchBagRelationships(Optional<Groups> groups) {
        return groups.map(this::fetchStudyUsers).map(this::fetchSubjects);
    }

    @Override
    public Page<Groups> fetchBagRelationships(Page<Groups> groups) {
        return new PageImpl<>(fetchBagRelationships(groups.getContent()), groups.getPageable(), groups.getTotalElements());
    }

    @Override
    public List<Groups> fetchBagRelationships(List<Groups> groups) {
        return Optional.of(groups).map(this::fetchStudyUsers).map(this::fetchSubjects).get();
    }

    Groups fetchStudyUsers(Groups result) {
        return entityManager
            .createQuery("select groups from Groups groups left join fetch groups.studyUsers where groups is :groups", Groups.class)
            .setParameter("groups", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Groups> fetchStudyUsers(List<Groups> groups) {
        return entityManager
            .createQuery(
                "select distinct groups from Groups groups left join fetch groups.studyUsers where groups in :groups",
                Groups.class
            )
            .setParameter("groups", groups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Groups fetchSubjects(Groups result) {
        return entityManager
            .createQuery("select groups from Groups groups left join fetch groups.subjects where groups is :groups", Groups.class)
            .setParameter("groups", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Groups> fetchSubjects(List<Groups> groups) {
        return entityManager
            .createQuery("select distinct groups from Groups groups left join fetch groups.subjects where groups in :groups", Groups.class)
            .setParameter("groups", groups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
