package uz.tatu.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.Units;

/**
 * Spring Data SQL repository for the Units entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitsRepository extends JpaRepository<Units, Long> {


}
