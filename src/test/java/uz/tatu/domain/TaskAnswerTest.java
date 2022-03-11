package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class TaskAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskAnswer.class);
        TaskAnswer taskAnswer1 = new TaskAnswer();
        taskAnswer1.setId(1L);
        TaskAnswer taskAnswer2 = new TaskAnswer();
        taskAnswer2.setId(taskAnswer1.getId());
        assertThat(taskAnswer1).isEqualTo(taskAnswer2);
        taskAnswer2.setId(2L);
        assertThat(taskAnswer1).isNotEqualTo(taskAnswer2);
        taskAnswer1.setId(null);
        assertThat(taskAnswer1).isNotEqualTo(taskAnswer2);
    }
}
