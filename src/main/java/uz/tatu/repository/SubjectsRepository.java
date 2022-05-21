package uz.tatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.Subjects;

/**
 * Spring Data SQL repository for the Subjects entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectsRepository extends JpaRepository<Subjects, Long> {

    Page<Subjects> findByGroups_Id(Long groupId, Pageable pageable);
}
