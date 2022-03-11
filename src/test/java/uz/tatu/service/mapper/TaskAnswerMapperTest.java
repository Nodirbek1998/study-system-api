package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskAnswerMapperTest {

    private TaskAnswerMapper taskAnswerMapper;

    @BeforeEach
    public void setUp() {
        taskAnswerMapper = new TaskAnswerMapperImpl();
    }
}
