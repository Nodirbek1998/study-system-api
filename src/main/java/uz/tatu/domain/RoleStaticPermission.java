package uz.tatu.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.enumeration.EnumStaticPermission;

/**
 * A RoleStaticPermission.
 */
@Data
@EqualsAndHashCode
@ToString
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
    private Role roles;


}
