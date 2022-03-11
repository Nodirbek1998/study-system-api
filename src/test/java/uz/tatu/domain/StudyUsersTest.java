package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class StudyUsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudyUsers.class);
        StudyUsers studyUsers1 = new StudyUsers();
        studyUsers1.setId(1L);
        StudyUsers studyUsers2 = new StudyUsers();
        studyUsers2.setId(studyUsers1.getId());
        assertThat(studyUsers1).isEqualTo(studyUsers2);
        studyUsers2.setId(2L);
        assertThat(studyUsers1).isNotEqualTo(studyUsers2);
        studyUsers1.setId(null);
        assertThat(studyUsers1).isNotEqualTo(studyUsers2);
    }
}
