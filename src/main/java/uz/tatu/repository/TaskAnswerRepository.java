package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.TaskAnswer;

/**
 * Spring Data SQL repository for the TaskAnswer entity.
 */
@Repository
public interface TaskAnswerRepository extends TaskAnswerRepositoryWithBagRelationships, JpaRepository<TaskAnswer, Long> {
    default Optional<TaskAnswer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<TaskAnswer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<TaskAnswer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
