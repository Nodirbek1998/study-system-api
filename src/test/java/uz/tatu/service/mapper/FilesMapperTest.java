package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FilesMapperTest {

    private FilesMapper filesMapper;

    @BeforeEach
    public void setUp() {
        filesMapper = new FilesMapperImpl();
    }
}
