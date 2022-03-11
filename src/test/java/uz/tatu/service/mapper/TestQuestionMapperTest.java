package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestQuestionMapperTest {

    private TestQuestionMapper testQuestionMapper;

    @BeforeEach
    public void setUp() {
        testQuestionMapper = new TestQuestionMapperImpl();
    }
}
