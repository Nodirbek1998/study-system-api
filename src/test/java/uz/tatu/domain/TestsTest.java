package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TestsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tests.class);
        Tests tests1 = new Tests();
        tests1.setId(1L);
        Tests tests2 = new Tests();
        tests2.setId(tests1.getId());
        assertThat(tests1).isEqualTo(tests2);
        tests2.setId(2L);
        assertThat(tests1).isNotEqualTo(tests2);
        tests1.setId(null);
        assertThat(tests1).isNotEqualTo(tests2);
    }
}
