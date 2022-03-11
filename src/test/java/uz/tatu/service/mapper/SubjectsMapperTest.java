package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubjectsMapperTest {

    private SubjectsMapper subjectsMapper;

    @BeforeEach
    public void setUp() {
        subjectsMapper = new SubjectsMapperImpl();
    }
}
