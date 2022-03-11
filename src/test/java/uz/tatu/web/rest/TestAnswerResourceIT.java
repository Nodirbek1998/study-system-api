package uz.tatu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.tatu.IntegrationTest;
import uz.tatu.domain.TestAnswer;
import uz.tatu.repository.TestAnswerRepository;
import uz.tatu.service.TestAnswerService;
import uz.tatu.service.dto.TestAnswerDTO;
import uz.tatu.service.mapper.TestAnswerMapper;

/**
 * Integration tests for the {@link TestAnswerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestAnswerResourceIT {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_RIGHT = false;
    private static final Boolean UPDATED_RIGHT = true;

    private static final String ENTITY_API_URL = "/api/test-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestAnswerRepository testAnswerRepository;

    @Mock
    private TestAnswerRepository testAnswerRepositoryMock;

    @Autowired
    private TestAnswerMapper testAnswerMapper;

    @Mock
    private TestAnswerService testAnswerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestAnswerMockMvc;

    private TestAnswer testAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestAnswer createEntity(EntityManager em) {
        TestAnswer testAnswer = new TestAnswer().createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT).right(DEFAULT_RIGHT);
        return testAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestAnswer createUpdatedEntity(EntityManager em) {
        TestAnswer testAnswer = new TestAnswer().createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).right(UPDATED_RIGHT);
        return testAnswer;
    }

    @BeforeEach
    public void initTest() {
        testAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createTestAnswer() throws Exception {
        int databaseSizeBeforeCreate = testAnswerRepository.findAll().size();
        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);
        restTestAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAnswerDTO)))
            .andExpect(status().isCreated());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        TestAnswer testTestAnswer = testAnswerList.get(testAnswerList.size() - 1);
        assertThat(testTestAnswer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTestAnswer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testTestAnswer.getRight()).isEqualTo(DEFAULT_RIGHT);
    }

    @Test
    @Transactional
    void createTestAnswerWithExistingId() throws Exception {
        // Create the TestAnswer with an existing ID
        testAnswer.setId(1L);
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        int databaseSizeBeforeCreate = testAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestAnswers() throws Exception {
        // Initialize the database
        testAnswerRepository.saveAndFlush(testAnswer);

        // Get all the testAnswerList
        restTestAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].right").value(hasItem(DEFAULT_RIGHT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestAnswersWithEagerRelationshipsIsEnabled() throws Exception {
        when(testAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testAnswerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestAnswersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(testAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testAnswerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTestAnswer() throws Exception {
        // Initialize the database
        testAnswerRepository.saveAndFlush(testAnswer);

        // Get the testAnswer
        restTestAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, testAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testAnswer.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.right").value(DEFAULT_RIGHT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestAnswer() throws Exception {
        // Get the testAnswer
        restTestAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTestAnswer() throws Exception {
        // Initialize the database
        testAnswerRepository.saveAndFlush(testAnswer);

        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();

        // Update the testAnswer
        TestAnswer updatedTestAnswer = testAnswerRepository.findById(testAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedTestAnswer are not directly saved in db
        em.detach(updatedTestAnswer);
        updatedTestAnswer.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).right(UPDATED_RIGHT);
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(updatedTestAnswer);

        restTestAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAnswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
        TestAnswer testTestAnswer = testAnswerList.get(testAnswerList.size() - 1);
        assertThat(testTestAnswer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestAnswer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestAnswer.getRight()).isEqualTo(UPDATED_RIGHT);
    }

    @Test
    @Transactional
    void putNonExistingTestAnswer() throws Exception {
        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();
        testAnswer.setId(count.incrementAndGet());

        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestAnswer() throws Exception {
        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();
        testAnswer.setId(count.incrementAndGet());

        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestAnswer() throws Exception {
        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();
        testAnswer.setId(count.incrementAndGet());

        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAnswerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestAnswerWithPatch() throws Exception {
        // Initialize the database
        testAnswerRepository.saveAndFlush(testAnswer);

        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();

        // Update the testAnswer using partial update
        TestAnswer partialUpdatedTestAnswer = new TestAnswer();
        partialUpdatedTestAnswer.setId(testAnswer.getId());

        partialUpdatedTestAnswer.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restTestAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
        TestAnswer testTestAnswer = testAnswerList.get(testAnswerList.size() - 1);
        assertThat(testTestAnswer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestAnswer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestAnswer.getRight()).isEqualTo(DEFAULT_RIGHT);
    }

    @Test
    @Transactional
    void fullUpdateTestAnswerWithPatch() throws Exception {
        // Initialize the database
        testAnswerRepository.saveAndFlush(testAnswer);

        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();

        // Update the testAnswer using partial update
        TestAnswer partialUpdatedTestAnswer = new TestAnswer();
        partialUpdatedTestAnswer.setId(testAnswer.getId());

        partialUpdatedTestAnswer.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT).right(UPDATED_RIGHT);

        restTestAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
        TestAnswer testTestAnswer = testAnswerList.get(testAnswerList.size() - 1);
        assertThat(testTestAnswer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTestAnswer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testTestAnswer.getRight()).isEqualTo(UPDATED_RIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingTestAnswer() throws Exception {
        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();
        testAnswer.setId(count.incrementAndGet());

        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testAnswerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestAnswer() throws Exception {
        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();
        testAnswer.setId(count.incrementAndGet());

        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestAnswer() throws Exception {
        int databaseSizeBeforeUpdate = testAnswerRepository.findAll().size();
        testAnswer.setId(count.incrementAndGet());

        // Create the TestAnswer
        TestAnswerDTO testAnswerDTO = testAnswerMapper.toDto(testAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestAnswer in the database
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestAnswer() throws Exception {
        // Initialize the database
        testAnswerRepository.saveAndFlush(testAnswer);

        int databaseSizeBeforeDelete = testAnswerRepository.findAll().size();

        // Delete the testAnswer
        restTestAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, testAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestAnswer> testAnswerList = testAnswerRepository.findAll();
        assertThat(testAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
