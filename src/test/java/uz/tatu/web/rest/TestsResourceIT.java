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
import uz.tatu.domain.Tests;
import uz.tatu.domain.enumeration.EnumTest;
import uz.tatu.repository.TestsRepository;
import uz.tatu.service.dto.TestsDTO;
import uz.tatu.service.mapper.TestsMapper;

/**
 * Integration tests for the {@link TestsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final EnumTest DEFAULT_STATUS = EnumTest.Yakuniy;
    private static final EnumTest UPDATED_STATUS = EnumTest.Unit;

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestsRepository testsRepository;

    @Autowired
    private TestsMapper testsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestsMockMvc;

    private Tests tests;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tests createEntity(EntityManager em) {
        Tests tests = new Tests().name(DEFAULT_NAME).status(DEFAULT_STATUS).deadline(DEFAULT_DEADLINE);
        return tests;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tests createUpdatedEntity(EntityManager em) {
        Tests tests = new Tests().name(UPDATED_NAME).status(UPDATED_STATUS).deadline(UPDATED_DEADLINE);
        return tests;
    }

    @BeforeEach
    public void initTest() {
        tests = createEntity(em);
    }

    @Test
    @Transactional
    void createTests() throws Exception {
        int databaseSizeBeforeCreate = testsRepository.findAll().size();
        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);
        restTestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testsDTO)))
            .andExpect(status().isCreated());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeCreate + 1);
        Tests testTests = testsList.get(testsList.size() - 1);
        assertThat(testTests.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTests.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTests.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    void createTestsWithExistingId() throws Exception {
        // Create the Tests with an existing ID
        tests.setId(1L);
        TestsDTO testsDTO = testsMapper.toDto(tests);

        int databaseSizeBeforeCreate = testsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTests() throws Exception {
        // Initialize the database
        testsRepository.saveAndFlush(tests);

        // Get all the testsList
        restTestsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tests.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())));
    }

    @Test
    @Transactional
    void getTests() throws Exception {
        // Initialize the database
        testsRepository.saveAndFlush(tests);

        // Get the tests
        restTestsMockMvc
            .perform(get(ENTITY_API_URL_ID, tests.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tests.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTests() throws Exception {
        // Get the tests
        restTestsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTests() throws Exception {
        // Initialize the database
        testsRepository.saveAndFlush(tests);

        int databaseSizeBeforeUpdate = testsRepository.findAll().size();

        // Update the tests
        Tests updatedTests = testsRepository.findById(tests.getId()).get();
        // Disconnect from session so that the updates on updatedTests are not directly saved in db
        em.detach(updatedTests);
        updatedTests.name(UPDATED_NAME).status(UPDATED_STATUS).deadline(UPDATED_DEADLINE);
        TestsDTO testsDTO = testsMapper.toDto(updatedTests);

        restTestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
        Tests testTests = testsList.get(testsList.size() - 1);
        assertThat(testTests.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTests.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTests.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void putNonExistingTests() throws Exception {
        int databaseSizeBeforeUpdate = testsRepository.findAll().size();
        tests.setId(count.incrementAndGet());

        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTests() throws Exception {
        int databaseSizeBeforeUpdate = testsRepository.findAll().size();
        tests.setId(count.incrementAndGet());

        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTests() throws Exception {
        int databaseSizeBeforeUpdate = testsRepository.findAll().size();
        tests.setId(count.incrementAndGet());

        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestsWithPatch() throws Exception {
        // Initialize the database
        testsRepository.saveAndFlush(tests);

        int databaseSizeBeforeUpdate = testsRepository.findAll().size();

        // Update the tests using partial update
        Tests partialUpdatedTests = new Tests();
        partialUpdatedTests.setId(tests.getId());

        partialUpdatedTests.name(UPDATED_NAME).status(UPDATED_STATUS).deadline(UPDATED_DEADLINE);

        restTestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTests.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTests))
            )
            .andExpect(status().isOk());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
        Tests testTests = testsList.get(testsList.size() - 1);
        assertThat(testTests.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTests.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTests.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void fullUpdateTestsWithPatch() throws Exception {
        // Initialize the database
        testsRepository.saveAndFlush(tests);

        int databaseSizeBeforeUpdate = testsRepository.findAll().size();

        // Update the tests using partial update
        Tests partialUpdatedTests = new Tests();
        partialUpdatedTests.setId(tests.getId());

        partialUpdatedTests.name(UPDATED_NAME).status(UPDATED_STATUS).deadline(UPDATED_DEADLINE);

        restTestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTests.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTests))
            )
            .andExpect(status().isOk());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
        Tests testTests = testsList.get(testsList.size() - 1);
        assertThat(testTests.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTests.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTests.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    void patchNonExistingTests() throws Exception {
        int databaseSizeBeforeUpdate = testsRepository.findAll().size();
        tests.setId(count.incrementAndGet());

        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTests() throws Exception {
        int databaseSizeBeforeUpdate = testsRepository.findAll().size();
        tests.setId(count.incrementAndGet());

        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTests() throws Exception {
        int databaseSizeBeforeUpdate = testsRepository.findAll().size();
        tests.setId(count.incrementAndGet());

        // Create the Tests
        TestsDTO testsDTO = testsMapper.toDto(tests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tests in the database
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTests() throws Exception {
        // Initialize the database
        testsRepository.saveAndFlush(tests);

        int databaseSizeBeforeDelete = testsRepository.findAll().size();

        // Delete the tests
        restTestsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tests.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tests> testsList = testsRepository.findAll();
        assertThat(testsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
