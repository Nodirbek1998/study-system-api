package uz.tatu.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.User;

/**
 * Spring Data SQL repository for the StudyUsers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudyUsersRepository extends JpaRepository<User, Long> {}
