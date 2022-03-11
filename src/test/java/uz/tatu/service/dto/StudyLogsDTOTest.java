package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class StudyLogsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyLogsDTO.class);
        StudyLogsDTO studyLogsDTO1 = new StudyLogsDTO();
        studyLogsDTO1.setId(1L);
        StudyLogsDTO studyLogsDTO2 = new StudyLogsDTO();
        assertThat(studyLogsDTO1).isNotEqualTo(studyLogsDTO2);
        studyLogsDTO2.setId(studyLogsDTO1.getId());
        assertThat(studyLogsDTO1).isEqualTo(studyLogsDTO2);
        studyLogsDTO2.setId(2L);
        assertThat(studyLogsDTO1).isNotEqualTo(studyLogsDTO2);
        studyLogsDTO1.setId(null);
        assertThat(studyLogsDTO1).isNotEqualTo(studyLogsDTO2);
    }
}
