package uz.tatu.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class RoleStaticPermissionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleStaticPermissionDTO.class);
        RoleStaticPermissionDTO roleStaticPermissionDTO1 = new RoleStaticPermissionDTO();
        roleStaticPermissionDTO1.setId(1L);
        RoleStaticPermissionDTO roleStaticPermissionDTO2 = new RoleStaticPermissionDTO();
        assertThat(roleStaticPermissionDTO1).isNotEqualTo(roleStaticPermissionDTO2);
        roleStaticPermissionDTO2.setId(roleStaticPermissionDTO1.getId());
        assertThat(roleStaticPermissionDTO1).isEqualTo(roleStaticPermissionDTO2);
        roleStaticPermissionDTO2.setId(2L);
        assertThat(roleStaticPermissionDTO1).isNotEqualTo(roleStaticPermissionDTO2);
        roleStaticPermissionDTO1.setId(null);
        assertThat(roleStaticPermissionDTO1).isNotEqualTo(roleStaticPermissionDTO2);
    }
}
