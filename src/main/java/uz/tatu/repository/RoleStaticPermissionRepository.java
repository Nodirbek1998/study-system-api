package uz.tatu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.tatu.domain.RoleStaticPermission;
import uz.tatu.domain.enumeration.EnumStaticPermission;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the RoleStaticPermission entity.
 */
@Repository
public interface RoleStaticPermissionRepository extends JpaRepository<RoleStaticPermission, Long>, JpaSpecificationExecutor<RoleStaticPermission> {

    Page<RoleStaticPermission> findByRoles_Id(Long roleId, Pageable pageable);

    void deleteByRoles_IdAndStaticPermission(Long roleId, EnumStaticPermission permission);

    Optional<RoleStaticPermission> findFirstByRoles_IdAndStaticPermission(Long roleId, EnumStaticPermission permission);

    List<RoleStaticPermission> findByRoles_Id(Long roleId);
}
