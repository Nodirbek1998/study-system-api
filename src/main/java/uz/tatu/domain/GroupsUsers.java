package uz.tatu.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import uz.tatu.domain.enumeration.UserType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "groups_users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GroupsUsers {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private User users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "group", allowSetters = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private Groups groups;

    @Size(max = 255)
    @Column(name = "user_type")
    private UserType userType;

}
