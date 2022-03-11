package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestsMapperTest {

    private TestsMapper testsMapper;

    @BeforeEach
    public void setUp() {
        testsMapper = new TestsMapperImpl();
    }
}
