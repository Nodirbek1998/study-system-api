package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class SubjectsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubjectsDTO.class);
        SubjectsDTO subjectsDTO1 = new SubjectsDTO();
        subjectsDTO1.setId(1L);
        SubjectsDTO subjectsDTO2 = new SubjectsDTO();
        assertThat(subjectsDTO1).isNotEqualTo(subjectsDTO2);
        subjectsDTO2.setId(subjectsDTO1.getId());
        assertThat(subjectsDTO1).isEqualTo(subjectsDTO2);
        subjectsDTO2.setId(2L);
        assertThat(subjectsDTO1).isNotEqualTo(subjectsDTO2);
        subjectsDTO1.setId(null);
        assertThat(subjectsDTO1).isNotEqualTo(subjectsDTO2);
    }
}
