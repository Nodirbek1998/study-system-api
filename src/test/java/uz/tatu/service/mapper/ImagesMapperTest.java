package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImagesMapperTest {

    private ImagesMapper imagesMapper;

    @BeforeEach
    public void setUp() {
        imagesMapper = new ImagesMapperImpl();
    }
}
