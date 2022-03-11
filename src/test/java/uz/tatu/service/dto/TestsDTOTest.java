package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TestsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestsDTO.class);
        TestsDTO testsDTO1 = new TestsDTO();
        testsDTO1.setId(1L);
        TestsDTO testsDTO2 = new TestsDTO();
        assertThat(testsDTO1).isNotEqualTo(testsDTO2);
        testsDTO2.setId(testsDTO1.getId());
        assertThat(testsDTO1).isEqualTo(testsDTO2);
        testsDTO2.setId(2L);
        assertThat(testsDTO1).isNotEqualTo(testsDTO2);
        testsDTO1.setId(null);
        assertThat(testsDTO1).isNotEqualTo(testsDTO2);
    }
}
