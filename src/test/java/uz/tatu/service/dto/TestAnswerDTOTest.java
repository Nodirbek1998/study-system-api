package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TestAnswerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestAnswerDTO.class);
        TestAnswerDTO testAnswerDTO1 = new TestAnswerDTO();
        testAnswerDTO1.setId(1L);
        TestAnswerDTO testAnswerDTO2 = new TestAnswerDTO();
        assertThat(testAnswerDTO1).isNotEqualTo(testAnswerDTO2);
        testAnswerDTO2.setId(testAnswerDTO1.getId());
        assertThat(testAnswerDTO1).isEqualTo(testAnswerDTO2);
        testAnswerDTO2.setId(2L);
        assertThat(testAnswerDTO1).isNotEqualTo(testAnswerDTO2);
        testAnswerDTO1.setId(null);
        assertThat(testAnswerDTO1).isNotEqualTo(testAnswerDTO2);
    }
}
