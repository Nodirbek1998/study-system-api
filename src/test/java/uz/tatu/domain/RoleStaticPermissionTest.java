package uz.tatu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.tatu.web.rest.TestUtil;

class RoleStaticPermissionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleStaticPermission.class);
        RoleStaticPermission roleStaticPermission1 = new RoleStaticPermission();
        roleStaticPermission1.setId(1L);
        RoleStaticPermission roleStaticPermission2 = new RoleStaticPermission();
        roleStaticPermission2.setId(roleStaticPermission1.getId());
        assertThat(roleStaticPermission1).isEqualTo(roleStaticPermission2);
        roleStaticPermission2.setId(2L);
        assertThat(roleStaticPermission1).isNotEqualTo(roleStaticPermission2);
        roleStaticPermission1.setId(null);
        assertThat(roleStaticPermission1).isNotEqualTo(roleStaticPermission2);
    }
}
