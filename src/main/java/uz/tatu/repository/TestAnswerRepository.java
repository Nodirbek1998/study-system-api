package uz.tatu.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.TestAnswer;

/**
 * Spring Data SQL repository for the TestAnswer entity.
 */
@Repository
public interface TestAnswerRepository extends TestAnswerRepositoryWithBagRelationships, JpaRepository<TestAnswer, Long> {
    default Optional<TestAnswer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<TestAnswer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<TestAnswer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
