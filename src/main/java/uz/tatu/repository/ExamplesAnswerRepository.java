package uz.tatu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.ExamplesAnswer;

@Repository
public interface ExamplesAnswerRepository extends JpaRepository<ExamplesAnswer, Long> {
}
