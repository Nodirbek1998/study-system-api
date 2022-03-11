package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TestAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestAnswer.class);
        TestAnswer testAnswer1 = new TestAnswer();
        testAnswer1.setId(1L);
        TestAnswer testAnswer2 = new TestAnswer();
        testAnswer2.setId(testAnswer1.getId());
        assertThat(testAnswer1).isEqualTo(testAnswer2);
        testAnswer2.setId(2L);
        assertThat(testAnswer1).isNotEqualTo(testAnswer2);
        testAnswer1.setId(null);
        assertThat(testAnswer1).isNotEqualTo(testAnswer2);
    }
}
