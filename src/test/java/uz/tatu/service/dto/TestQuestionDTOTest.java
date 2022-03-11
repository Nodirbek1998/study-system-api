package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TestQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestQuestionDTO.class);
        TestQuestionDTO testQuestionDTO1 = new TestQuestionDTO();
        testQuestionDTO1.setId(1L);
        TestQuestionDTO testQuestionDTO2 = new TestQuestionDTO();
        assertThat(testQuestionDTO1).isNotEqualTo(testQuestionDTO2);
        testQuestionDTO2.setId(testQuestionDTO1.getId());
        assertThat(testQuestionDTO1).isEqualTo(testQuestionDTO2);
        testQuestionDTO2.setId(2L);
        assertThat(testQuestionDTO1).isNotEqualTo(testQuestionDTO2);
        testQuestionDTO1.setId(null);
        assertThat(testQuestionDTO1).isNotEqualTo(testQuestionDTO2);
    }
}
