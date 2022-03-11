package uz.tatu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import uz.tatu.domain.Units;
import uz.tatu.repository.UnitsRepository;
import uz.tatu.service.dto.UnitsDTO;
import uz.tatu.service.mapper.UnitsMapper;

/**
 * Integration tests for the {@link UnitsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitsResourceIT {

    private static final String DEFAULT_NAME_UZ = "AAAAAAAAAA";
    private static final String UPDATED_NAME_UZ = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_RU = "AAAAAAAAAA";
    private static final String UPDATED_NAME_RU = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitsRepository unitsRepository;

    @Autowired
    private UnitsMapper unitsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitsMockMvc;

    private Units units;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Units createEntity(EntityManager em) {
        Units units = new Units()
            .nameUz(DEFAULT_NAME_UZ)
            .nameRu(DEFAULT_NAME_RU)
            .nameEn(DEFAULT_NAME_EN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return units;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Units createUpdatedEntity(EntityManager em) {
        Units units = new Units()
            .nameUz(UPDATED_NAME_UZ)
            .nameRu(UPDATED_NAME_RU)
            .nameEn(UPDATED_NAME_EN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return units;
    }

    @BeforeEach
    public void initTest() {
        units = createEntity(em);
    }

    @Test
    @Transactional
    void createUnits() throws Exception {
        int databaseSizeBeforeCreate = unitsRepository.findAll().size();
        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);
        restUnitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitsDTO)))
            .andExpect(status().isCreated());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeCreate + 1);
        Units testUnits = unitsList.get(unitsList.size() - 1);
        assertThat(testUnits.getNameUz()).isEqualTo(DEFAULT_NAME_UZ);
        assertThat(testUnits.getNameRu()).isEqualTo(DEFAULT_NAME_RU);
        assertThat(testUnits.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testUnits.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUnits.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUnitsWithExistingId() throws Exception {
        // Create the Units with an existing ID
        units.setId(1L);
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        int databaseSizeBeforeCreate = unitsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUnits() throws Exception {
        // Initialize the database
        unitsRepository.saveAndFlush(units);

        // Get all the unitsList
        restUnitsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(units.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameUz").value(hasItem(DEFAULT_NAME_UZ)))
            .andExpect(jsonPath("$.[*].nameRu").value(hasItem(DEFAULT_NAME_RU)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getUnits() throws Exception {
        // Initialize the database
        unitsRepository.saveAndFlush(units);

        // Get the units
        restUnitsMockMvc
            .perform(get(ENTITY_API_URL_ID, units.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(units.getId().intValue()))
            .andExpect(jsonPath("$.nameUz").value(DEFAULT_NAME_UZ))
            .andExpect(jsonPath("$.nameRu").value(DEFAULT_NAME_RU))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUnits() throws Exception {
        // Get the units
        restUnitsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUnits() throws Exception {
        // Initialize the database
        unitsRepository.saveAndFlush(units);

        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();

        // Update the units
        Units updatedUnits = unitsRepository.findById(units.getId()).get();
        // Disconnect from session so that the updates on updatedUnits are not directly saved in db
        em.detach(updatedUnits);
        updatedUnits
            .nameUz(UPDATED_NAME_UZ)
            .nameRu(UPDATED_NAME_RU)
            .nameEn(UPDATED_NAME_EN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        UnitsDTO unitsDTO = unitsMapper.toDto(updatedUnits);

        restUnitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
        Units testUnits = unitsList.get(unitsList.size() - 1);
        assertThat(testUnits.getNameUz()).isEqualTo(UPDATED_NAME_UZ);
        assertThat(testUnits.getNameRu()).isEqualTo(UPDATED_NAME_RU);
        assertThat(testUnits.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testUnits.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUnits.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUnits() throws Exception {
        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();
        units.setId(count.incrementAndGet());

        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnits() throws Exception {
        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();
        units.setId(count.incrementAndGet());

        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnits() throws Exception {
        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();
        units.setId(count.incrementAndGet());

        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitsWithPatch() throws Exception {
        // Initialize the database
        unitsRepository.saveAndFlush(units);

        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();

        // Update the units using partial update
        Units partialUpdatedUnits = new Units();
        partialUpdatedUnits.setId(units.getId());

        partialUpdatedUnits.nameUz(UPDATED_NAME_UZ).nameRu(UPDATED_NAME_RU).nameEn(UPDATED_NAME_EN).updatedAt(UPDATED_UPDATED_AT);

        restUnitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnits.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnits))
            )
            .andExpect(status().isOk());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
        Units testUnits = unitsList.get(unitsList.size() - 1);
        assertThat(testUnits.getNameUz()).isEqualTo(UPDATED_NAME_UZ);
        assertThat(testUnits.getNameRu()).isEqualTo(UPDATED_NAME_RU);
        assertThat(testUnits.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testUnits.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUnits.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUnitsWithPatch() throws Exception {
        // Initialize the database
        unitsRepository.saveAndFlush(units);

        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();

        // Update the units using partial update
        Units partialUpdatedUnits = new Units();
        partialUpdatedUnits.setId(units.getId());

        partialUpdatedUnits
            .nameUz(UPDATED_NAME_UZ)
            .nameRu(UPDATED_NAME_RU)
            .nameEn(UPDATED_NAME_EN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUnitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnits.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnits))
            )
            .andExpect(status().isOk());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
        Units testUnits = unitsList.get(unitsList.size() - 1);
        assertThat(testUnits.getNameUz()).isEqualTo(UPDATED_NAME_UZ);
        assertThat(testUnits.getNameRu()).isEqualTo(UPDATED_NAME_RU);
        assertThat(testUnits.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testUnits.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUnits.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUnits() throws Exception {
        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();
        units.setId(count.incrementAndGet());

        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnits() throws Exception {
        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();
        units.setId(count.incrementAndGet());

        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnits() throws Exception {
        int databaseSizeBeforeUpdate = unitsRepository.findAll().size();
        units.setId(count.incrementAndGet());

        // Create the Units
        UnitsDTO unitsDTO = unitsMapper.toDto(units);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(unitsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Units in the database
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnits() throws Exception {
        // Initialize the database
        unitsRepository.saveAndFlush(units);

        int databaseSizeBeforeDelete = unitsRepository.findAll().size();

        // Delete the units
        restUnitsMockMvc
            .perform(delete(ENTITY_API_URL_ID, units.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Units> unitsList = unitsRepository.findAll();
        assertThat(unitsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
