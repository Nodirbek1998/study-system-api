package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudyUsersMapperTest {

    private StudyUsersMapper studyUsersMapper;

    @BeforeEach
    public void setUp() {
        studyUsersMapper = new StudyUsersMapperImpl();
    }
}
