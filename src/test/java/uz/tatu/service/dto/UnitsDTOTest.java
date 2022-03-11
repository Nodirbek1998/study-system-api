package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class UnitsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitsDTO.class);
        UnitsDTO unitsDTO1 = new UnitsDTO();
        unitsDTO1.setId(1L);
        UnitsDTO unitsDTO2 = new UnitsDTO();
        assertThat(unitsDTO1).isNotEqualTo(unitsDTO2);
        unitsDTO2.setId(unitsDTO1.getId());
        assertThat(unitsDTO1).isEqualTo(unitsDTO2);
        unitsDTO2.setId(2L);
        assertThat(unitsDTO1).isNotEqualTo(unitsDTO2);
        unitsDTO1.setId(null);
        assertThat(unitsDTO1).isNotEqualTo(unitsDTO2);
    }
}
