package uz.tatu.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.domain.UserRole;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {

    String USERS_ROLE_BY_ID_CACHE = "usersRoleByUserID";

    @Modifying
    @Transactional
    void deleteAllByUser_Id(Long userId);

    List<UserRole> findByUser_Id(Long id);

    @Cacheable(cacheNames = USERS_ROLE_BY_ID_CACHE)
    Optional<UserRole> findFirstByUser_Id(Long userId);
}
