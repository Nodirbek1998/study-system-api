package uz.tatu.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.RoleStaticPermission;

/**
 * Spring Data SQL repository for the RoleStaticPermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleStaticPermissionRepository extends JpaRepository<RoleStaticPermission, Long> {}
