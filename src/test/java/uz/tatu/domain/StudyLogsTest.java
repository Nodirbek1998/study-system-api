package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class StudyLogsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyLogs.class);
        StudyLogs studyLogs1 = new StudyLogs();
        studyLogs1.setId(1L);
        StudyLogs studyLogs2 = new StudyLogs();
        studyLogs2.setId(studyLogs1.getId());
        assertThat(studyLogs1).isEqualTo(studyLogs2);
        studyLogs2.setId(2L);
        assertThat(studyLogs1).isNotEqualTo(studyLogs2);
        studyLogs1.setId(null);
        assertThat(studyLogs1).isNotEqualTo(studyLogs2);
    }
}
