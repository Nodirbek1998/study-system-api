package uz.tatu.service.dto;

import java.io.Serializable;
import java.util.Objects;
import uz.tatu.domain.enumeration.EnumStaticPermission;

/**
 * A DTO for the {@link uz.tatu.domain.RoleStaticPermission} entity.
 */
public class RoleStaticPermissionDTO implements Serializable {

    private Long id;

    private EnumStaticPermission staticPermission;

    private RoleDTO role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumStaticPermission getStaticPermission() {
        return staticPermission;
    }

    public void setStaticPermission(EnumStaticPermission staticPermission) {
        this.staticPermission = staticPermission;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleStaticPermissionDTO)) {
            return false;
        }

        RoleStaticPermissionDTO roleStaticPermissionDTO = (RoleStaticPermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleStaticPermissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleStaticPermissionDTO{" +
            "id=" + getId() +
            ", staticPermission='" + getStaticPermission() + "'" +
            ", role=" + getRole() +
            "}";
    }
}
