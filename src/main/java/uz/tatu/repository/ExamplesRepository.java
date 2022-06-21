package uz.tatu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.Examples;

@Repository
public interface ExamplesRepository extends JpaRepository<Examples, Long> {
}
