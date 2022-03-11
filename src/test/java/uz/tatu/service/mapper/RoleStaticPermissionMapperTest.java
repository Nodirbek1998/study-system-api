package uz.tatu.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleStaticPermissionMapperTest {

    private RoleStaticPermissionMapper roleStaticPermissionMapper;

    @BeforeEach
    public void setUp() {
        roleStaticPermissionMapper = new RoleStaticPermissionMapperImpl();
    }
}
