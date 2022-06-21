package uz.tatu.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "subject_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubjectsGroups {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "subjects", allowSetters = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private Subjects subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "group", allowSetters = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private Groups group;

}
