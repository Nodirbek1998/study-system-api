package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.tatu.domain.audit.DateAudit;

/**
 * A TestQuestion.
 */
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "test_question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestQuestion extends DateAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private Integer level;

    @Column(name = "answer_a")
    private Boolean answerA;

    @Column(name = "answer_b")
    private Boolean answerB;

    @Column(name = "answer_c")
    private Boolean answerC;

    @Column(name = "answer_d")
    private Boolean answerD;

    @ManyToOne
    @JsonIgnoreProperties(value = { "test" }, allowSetters = true)
    private Tests test;

}
