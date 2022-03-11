package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class StudyUsersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyUsersDTO.class);
        StudyUsersDTO studyUsersDTO1 = new StudyUsersDTO();
        studyUsersDTO1.setId(1L);
        StudyUsersDTO studyUsersDTO2 = new StudyUsersDTO();
        assertThat(studyUsersDTO1).isNotEqualTo(studyUsersDTO2);
        studyUsersDTO2.setId(studyUsersDTO1.getId());
        assertThat(studyUsersDTO1).isEqualTo(studyUsersDTO2);
        studyUsersDTO2.setId(2L);
        assertThat(studyUsersDTO1).isNotEqualTo(studyUsersDTO2);
        studyUsersDTO1.setId(null);
        assertThat(studyUsersDTO1).isNotEqualTo(studyUsersDTO2);
    }
}
