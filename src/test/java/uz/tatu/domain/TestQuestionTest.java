package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TestQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestQuestion.class);
        TestQuestion testQuestion1 = new TestQuestion();
        testQuestion1.setId(1L);
        TestQuestion testQuestion2 = new TestQuestion();
        testQuestion2.setId(testQuestion1.getId());
        assertThat(testQuestion1).isEqualTo(testQuestion2);
        testQuestion2.setId(2L);
        assertThat(testQuestion1).isNotEqualTo(testQuestion2);
        testQuestion1.setId(null);
        assertThat(testQuestion1).isNotEqualTo(testQuestion2);
    }
}
