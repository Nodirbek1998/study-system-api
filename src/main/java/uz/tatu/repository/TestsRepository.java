package uz.tatu.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.Tests;

/**
 * Spring Data SQL repository for the Tests entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestsRepository extends JpaRepository<Tests, Long> {}
