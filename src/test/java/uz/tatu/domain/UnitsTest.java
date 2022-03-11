package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class UnitsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Units.class);
        Units units1 = new Units();
        units1.setId(1L);
        Units units2 = new Units();
        units2.setId(units1.getId());
        assertThat(units1).isEqualTo(units2);
        units2.setId(2L);
        assertThat(units1).isNotEqualTo(units2);
        units1.setId(null);
        assertThat(units1).isNotEqualTo(units2);
    }
}
