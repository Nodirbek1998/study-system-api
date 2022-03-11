package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAnswerMapperTest {

    private TestAnswerMapper testAnswerMapper;

    @BeforeEach
    public void setUp() {
        testAnswerMapper = new TestAnswerMapperImpl();
    }
}
