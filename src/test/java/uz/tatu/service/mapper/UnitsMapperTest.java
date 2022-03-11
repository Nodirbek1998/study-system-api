package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitsMapperTest {

    private UnitsMapper unitsMapper;

    @BeforeEach
    public void setUp() {
        unitsMapper = new UnitsMapperImpl();
    }
}
