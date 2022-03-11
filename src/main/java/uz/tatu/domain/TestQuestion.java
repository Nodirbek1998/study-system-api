package uz.tatu.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestQuestion.
 */
@Entity
@Table(name = "test_question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestQuestion implements Serializable {

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
    @JsonIgnoreProperties(value = { "subject" }, allowSetters = true)
    private Tests test;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TestQuestion name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return this.level;
    }

    public TestQuestion level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getAnswerA() {
        return this.answerA;
    }

    public TestQuestion answerA(Boolean answerA) {
        this.setAnswerA(answerA);
        return this;
    }

    public void setAnswerA(Boolean answerA) {
        this.answerA = answerA;
    }

    public Boolean getAnswerB() {
        return this.answerB;
    }

    public TestQuestion answerB(Boolean answerB) {
        this.setAnswerB(answerB);
        return this;
    }

    public void setAnswerB(Boolean answerB) {
        this.answerB = answerB;
    }

    public Boolean getAnswerC() {
        return this.answerC;
    }

    public TestQuestion answerC(Boolean answerC) {
        this.setAnswerC(answerC);
        return this;
    }

    public void setAnswerC(Boolean answerC) {
        this.answerC = answerC;
    }

    public Boolean getAnswerD() {
        return this.answerD;
    }

    public TestQuestion answerD(Boolean answerD) {
        this.setAnswerD(answerD);
        return this;
    }

    public void setAnswerD(Boolean answerD) {
        this.answerD = answerD;
    }

    public Tests getTest() {
        return this.test;
    }

    public void setTest(Tests tests) {
        this.test = tests;
    }

    public TestQuestion test(Tests tests) {
        this.setTest(tests);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestQuestion)) {
            return false;
        }
        return id != null && id.equals(((TestQuestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestQuestion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            ", answerA='" + getAnswerA() + "'" +
            ", answerB='" + getAnswerB() + "'" +
            ", answerC='" + getAnswerC() + "'" +
            ", answerD='" + getAnswerD() + "'" +
            "}";
    }
}
