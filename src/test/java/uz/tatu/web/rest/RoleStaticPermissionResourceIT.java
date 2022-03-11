package uz.tatu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.IntegrationTest;
import uz.tatu.domain.RoleStaticPermission;
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.repository.RoleStaticPermissionRepository;
import uz.tatu.service.dto.RoleStaticPermissionDTO;
import uz.tatu.service.mapper.RoleStaticPermissionMapper;

/**
 * Integration tests for the {@link RoleStaticPermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleStaticPermissionResourceIT {

    private static final EnumStaticPermission DEFAULT_STATIC_PERMISSION = EnumStaticPermission.Add;
    private static final EnumStaticPermission UPDATED_STATIC_PERMISSION = EnumStaticPermission.Delete;

    private static final String ENTITY_API_URL = "/api/role-static-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleStaticPermissionRepository roleStaticPermissionRepository;

    @Autowired
    private RoleStaticPermissionMapper roleStaticPermissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleStaticPermissionMockMvc;

    private RoleStaticPermission roleStaticPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleStaticPermission createEntity(EntityManager em) {
        RoleStaticPermission roleStaticPermission = new RoleStaticPermission().staticPermission(DEFAULT_STATIC_PERMISSION);
        return roleStaticPermission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleStaticPermission createUpdatedEntity(EntityManager em) {
        RoleStaticPermission roleStaticPermission = new RoleStaticPermission().staticPermission(UPDATED_STATIC_PERMISSION);
        return roleStaticPermission;
    }

    @BeforeEach
    public void initTest() {
        roleStaticPermission = createEntity(em);
    }

    @Test
    @Transactional
    void createRoleStaticPermission() throws Exception {
        int databaseSizeBeforeCreate = roleStaticPermissionRepository.findAll().size();
        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);
        restRoleStaticPermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeCreate + 1);
        RoleStaticPermission testRoleStaticPermission = roleStaticPermissionList.get(roleStaticPermissionList.size() - 1);
        assertThat(testRoleStaticPermission.getStaticPermission()).isEqualTo(DEFAULT_STATIC_PERMISSION);
    }

    @Test
    @Transactional
    void createRoleStaticPermissionWithExistingId() throws Exception {
        // Create the RoleStaticPermission with an existing ID
        roleStaticPermission.setId(1L);
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        int databaseSizeBeforeCreate = roleStaticPermissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleStaticPermissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoleStaticPermissions() throws Exception {
        // Initialize the database
        roleStaticPermissionRepository.saveAndFlush(roleStaticPermission);

        // Get all the roleStaticPermissionList
        restRoleStaticPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleStaticPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].staticPermission").value(hasItem(DEFAULT_STATIC_PERMISSION.toString())));
    }

    @Test
    @Transactional
    void getRoleStaticPermission() throws Exception {
        // Initialize the database
        roleStaticPermissionRepository.saveAndFlush(roleStaticPermission);

        // Get the roleStaticPermission
        restRoleStaticPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, roleStaticPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleStaticPermission.getId().intValue()))
            .andExpect(jsonPath("$.staticPermission").value(DEFAULT_STATIC_PERMISSION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoleStaticPermission() throws Exception {
        // Get the roleStaticPermission
        restRoleStaticPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoleStaticPermission() throws Exception {
        // Initialize the database
        roleStaticPermissionRepository.saveAndFlush(roleStaticPermission);

        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();

        // Update the roleStaticPermission
        RoleStaticPermission updatedRoleStaticPermission = roleStaticPermissionRepository.findById(roleStaticPermission.getId()).get();
        // Disconnect from session so that the updates on updatedRoleStaticPermission are not directly saved in db
        em.detach(updatedRoleStaticPermission);
        updatedRoleStaticPermission.staticPermission(UPDATED_STATIC_PERMISSION);
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(updatedRoleStaticPermission);

        restRoleStaticPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleStaticPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
        RoleStaticPermission testRoleStaticPermission = roleStaticPermissionList.get(roleStaticPermissionList.size() - 1);
        assertThat(testRoleStaticPermission.getStaticPermission()).isEqualTo(UPDATED_STATIC_PERMISSION);
    }

    @Test
    @Transactional
    void putNonExistingRoleStaticPermission() throws Exception {
        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();
        roleStaticPermission.setId(count.incrementAndGet());

        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleStaticPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleStaticPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleStaticPermission() throws Exception {
        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();
        roleStaticPermission.setId(count.incrementAndGet());

        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleStaticPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleStaticPermission() throws Exception {
        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();
        roleStaticPermission.setId(count.incrementAndGet());

        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleStaticPermissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleStaticPermissionWithPatch() throws Exception {
        // Initialize the database
        roleStaticPermissionRepository.saveAndFlush(roleStaticPermission);

        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();

        // Update the roleStaticPermission using partial update
        RoleStaticPermission partialUpdatedRoleStaticPermission = new RoleStaticPermission();
        partialUpdatedRoleStaticPermission.setId(roleStaticPermission.getId());

        restRoleStaticPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleStaticPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleStaticPermission))
            )
            .andExpect(status().isOk());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
        RoleStaticPermission testRoleStaticPermission = roleStaticPermissionList.get(roleStaticPermissionList.size() - 1);
        assertThat(testRoleStaticPermission.getStaticPermission()).isEqualTo(DEFAULT_STATIC_PERMISSION);
    }

    @Test
    @Transactional
    void fullUpdateRoleStaticPermissionWithPatch() throws Exception {
        // Initialize the database
        roleStaticPermissionRepository.saveAndFlush(roleStaticPermission);

        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();

        // Update the roleStaticPermission using partial update
        RoleStaticPermission partialUpdatedRoleStaticPermission = new RoleStaticPermission();
        partialUpdatedRoleStaticPermission.setId(roleStaticPermission.getId());

        partialUpdatedRoleStaticPermission.staticPermission(UPDATED_STATIC_PERMISSION);

        restRoleStaticPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleStaticPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleStaticPermission))
            )
            .andExpect(status().isOk());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
        RoleStaticPermission testRoleStaticPermission = roleStaticPermissionList.get(roleStaticPermissionList.size() - 1);
        assertThat(testRoleStaticPermission.getStaticPermission()).isEqualTo(UPDATED_STATIC_PERMISSION);
    }

    @Test
    @Transactional
    void patchNonExistingRoleStaticPermission() throws Exception {
        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();
        roleStaticPermission.setId(count.incrementAndGet());

        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleStaticPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleStaticPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleStaticPermission() throws Exception {
        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();
        roleStaticPermission.setId(count.incrementAndGet());

        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleStaticPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleStaticPermission() throws Exception {
        int databaseSizeBeforeUpdate = roleStaticPermissionRepository.findAll().size();
        roleStaticPermission.setId(count.incrementAndGet());

        // Create the RoleStaticPermission
        RoleStaticPermissionDTO roleStaticPermissionDTO = roleStaticPermissionMapper.toDto(roleStaticPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleStaticPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleStaticPermissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleStaticPermission in the database
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoleStaticPermission() throws Exception {
        // Initialize the database
        roleStaticPermissionRepository.saveAndFlush(roleStaticPermission);

        int databaseSizeBeforeDelete = roleStaticPermissionRepository.findAll().size();

        // Delete the roleStaticPermission
        restRoleStaticPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleStaticPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoleStaticPermission> roleStaticPermissionList = roleStaticPermissionRepository.findAll();
        assertThat(roleStaticPermissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
