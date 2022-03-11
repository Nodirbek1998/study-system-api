package uz.tatu.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.enumeration.EnumStaticPermission;

/**
 * A RoleStaticPermission.
 */
@Entity
@Table(name = "role_static_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoleStaticPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "static_permission")
    private EnumStaticPermission staticPermission;

    @ManyToOne
    private Role role;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoleStaticPermission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumStaticPermission getStaticPermission() {
        return this.staticPermission;
    }

    public RoleStaticPermission staticPermission(EnumStaticPermission staticPermission) {
        this.setStaticPermission(staticPermission);
        return this;
    }

    public void setStaticPermission(EnumStaticPermission staticPermission) {
        this.staticPermission = staticPermission;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RoleStaticPermission role(Role role) {
        this.setRole(role);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleStaticPermission)) {
            return false;
        }
        return id != null && id.equals(((RoleStaticPermission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleStaticPermission{" +
            "id=" + getId() +
            ", staticPermission='" + getStaticPermission() + "'" +
            "}";
    }
}
