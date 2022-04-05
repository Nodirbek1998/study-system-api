package uz.tatu.service.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uz.tatu.domain.enumeration.EnumStaticPermission;

/**
 * A DTO for the {@link uz.tatu.domain.RoleStaticPermission} entity.
 */
@Data
@EqualsAndHashCode
@ToString
public class RoleStaticPermissionDTO implements Serializable {

    private Long id;

    private EnumStaticPermission staticPermission;

    private Long roleId;

    private List<EnumStaticPermission> staticPermissions;

}
