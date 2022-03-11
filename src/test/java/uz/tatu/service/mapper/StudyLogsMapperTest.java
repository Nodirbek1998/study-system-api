package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudyLogsMapperTest {

    private StudyLogsMapper studyLogsMapper;

    @BeforeEach
    public void setUp() {
        studyLogsMapper = new StudyLogsMapperImpl();
    }
}
