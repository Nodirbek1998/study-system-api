package uz.tatu.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link uz.tatu.domain.TestQuestion} entity.
 */
public class TestQuestionDTO implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private Boolean answerA;

    private Boolean answerB;

    private Boolean answerC;

    private Boolean answerD;

    private TestsDTO test;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getAnswerA() {
        return answerA;
    }

    public void setAnswerA(Boolean answerA) {
        this.answerA = answerA;
    }

    public Boolean getAnswerB() {
        return answerB;
    }

    public void setAnswerB(Boolean answerB) {
        this.answerB = answerB;
    }

    public Boolean getAnswerC() {
        return answerC;
    }

    public void setAnswerC(Boolean answerC) {
        this.answerC = answerC;
    }

    public Boolean getAnswerD() {
        return answerD;
    }

    public void setAnswerD(Boolean answerD) {
        this.answerD = answerD;
    }

    public TestsDTO getTest() {
        return test;
    }

    public void setTest(TestsDTO test) {
        this.test = test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestQuestionDTO)) {
            return false;
        }

        TestQuestionDTO testQuestionDTO = (TestQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestQuestionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            ", answerA='" + getAnswerA() + "'" +
            ", answerB='" + getAnswerB() + "'" +
            ", answerC='" + getAnswerC() + "'" +
            ", answerD='" + getAnswerD() + "'" +
            ", test=" + getTest() +
            "}";
    }
}
