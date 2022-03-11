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
import uz.tatu.domain.TestQuestion;
import uz.tatu.repository.TestQuestionRepository;
import uz.tatu.service.dto.TestQuestionDTO;
import uz.tatu.service.mapper.TestQuestionMapper;

/**
 * Integration tests for the {@link TestQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestQuestionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Boolean DEFAULT_ANSWER_A = false;
    private static final Boolean UPDATED_ANSWER_A = true;

    private static final Boolean DEFAULT_ANSWER_B = false;
    private static final Boolean UPDATED_ANSWER_B = true;

    private static final Boolean DEFAULT_ANSWER_C = false;
    private static final Boolean UPDATED_ANSWER_C = true;

    private static final Boolean DEFAULT_ANSWER_D = false;
    private static final Boolean UPDATED_ANSWER_D = true;

    private static final String ENTITY_API_URL = "/api/test-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestQuestionRepository testQuestionRepository;

    @Autowired
    private TestQuestionMapper testQuestionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestQuestionMockMvc;

    private TestQuestion testQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestQuestion createEntity(EntityManager em) {
        TestQuestion testQuestion = new TestQuestion()
            .name(DEFAULT_NAME)
            .level(DEFAULT_LEVEL)
            .answerA(DEFAULT_ANSWER_A)
            .answerB(DEFAULT_ANSWER_B)
            .answerC(DEFAULT_ANSWER_C)
            .answerD(DEFAULT_ANSWER_D);
        return testQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestQuestion createUpdatedEntity(EntityManager em) {
        TestQuestion testQuestion = new TestQuestion()
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .answerD(UPDATED_ANSWER_D);
        return testQuestion;
    }

    @BeforeEach
    public void initTest() {
        testQuestion = createEntity(em);
    }

    @Test
    @Transactional
    void createTestQuestion() throws Exception {
        int databaseSizeBeforeCreate = testQuestionRepository.findAll().size();
        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);
        restTestQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        TestQuestion testTestQuestion = testQuestionList.get(testQuestionList.size() - 1);
        assertThat(testTestQuestion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestQuestion.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testTestQuestion.getAnswerA()).isEqualTo(DEFAULT_ANSWER_A);
        assertThat(testTestQuestion.getAnswerB()).isEqualTo(DEFAULT_ANSWER_B);
        assertThat(testTestQuestion.getAnswerC()).isEqualTo(DEFAULT_ANSWER_C);
        assertThat(testTestQuestion.getAnswerD()).isEqualTo(DEFAULT_ANSWER_D);
    }

    @Test
    @Transactional
    void createTestQuestionWithExistingId() throws Exception {
        // Create the TestQuestion with an existing ID
        testQuestion.setId(1L);
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        int databaseSizeBeforeCreate = testQuestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestQuestions() throws Exception {
        // Initialize the database
        testQuestionRepository.saveAndFlush(testQuestion);

        // Get all the testQuestionList
        restTestQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].answerA").value(hasItem(DEFAULT_ANSWER_A.booleanValue())))
            .andExpect(jsonPath("$.[*].answerB").value(hasItem(DEFAULT_ANSWER_B.booleanValue())))
            .andExpect(jsonPath("$.[*].answerC").value(hasItem(DEFAULT_ANSWER_C.booleanValue())))
            .andExpect(jsonPath("$.[*].answerD").value(hasItem(DEFAULT_ANSWER_D.booleanValue())));
    }

    @Test
    @Transactional
    void getTestQuestion() throws Exception {
        // Initialize the database
        testQuestionRepository.saveAndFlush(testQuestion);

        // Get the testQuestion
        restTestQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, testQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testQuestion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.answerA").value(DEFAULT_ANSWER_A.booleanValue()))
            .andExpect(jsonPath("$.answerB").value(DEFAULT_ANSWER_B.booleanValue()))
            .andExpect(jsonPath("$.answerC").value(DEFAULT_ANSWER_C.booleanValue()))
            .andExpect(jsonPath("$.answerD").value(DEFAULT_ANSWER_D.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestQuestion() throws Exception {
        // Get the testQuestion
        restTestQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTestQuestion() throws Exception {
        // Initialize the database
        testQuestionRepository.saveAndFlush(testQuestion);

        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();

        // Update the testQuestion
        TestQuestion updatedTestQuestion = testQuestionRepository.findById(testQuestion.getId()).get();
        // Disconnect from session so that the updates on updatedTestQuestion are not directly saved in db
        em.detach(updatedTestQuestion);
        updatedTestQuestion
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .answerD(UPDATED_ANSWER_D);
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(updatedTestQuestion);

        restTestQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
        TestQuestion testTestQuestion = testQuestionList.get(testQuestionList.size() - 1);
        assertThat(testTestQuestion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestQuestion.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTestQuestion.getAnswerA()).isEqualTo(UPDATED_ANSWER_A);
        assertThat(testTestQuestion.getAnswerB()).isEqualTo(UPDATED_ANSWER_B);
        assertThat(testTestQuestion.getAnswerC()).isEqualTo(UPDATED_ANSWER_C);
        assertThat(testTestQuestion.getAnswerD()).isEqualTo(UPDATED_ANSWER_D);
    }

    @Test
    @Transactional
    void putNonExistingTestQuestion() throws Exception {
        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();
        testQuestion.setId(count.incrementAndGet());

        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestQuestion() throws Exception {
        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();
        testQuestion.setId(count.incrementAndGet());

        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestQuestion() throws Exception {
        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();
        testQuestion.setId(count.incrementAndGet());

        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestQuestionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestQuestionWithPatch() throws Exception {
        // Initialize the database
        testQuestionRepository.saveAndFlush(testQuestion);

        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();

        // Update the testQuestion using partial update
        TestQuestion partialUpdatedTestQuestion = new TestQuestion();
        partialUpdatedTestQuestion.setId(testQuestion.getId());

        partialUpdatedTestQuestion.level(UPDATED_LEVEL).answerA(UPDATED_ANSWER_A).answerD(UPDATED_ANSWER_D);

        restTestQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestQuestion))
            )
            .andExpect(status().isOk());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
        TestQuestion testTestQuestion = testQuestionList.get(testQuestionList.size() - 1);
        assertThat(testTestQuestion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTestQuestion.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTestQuestion.getAnswerA()).isEqualTo(UPDATED_ANSWER_A);
        assertThat(testTestQuestion.getAnswerB()).isEqualTo(DEFAULT_ANSWER_B);
        assertThat(testTestQuestion.getAnswerC()).isEqualTo(DEFAULT_ANSWER_C);
        assertThat(testTestQuestion.getAnswerD()).isEqualTo(UPDATED_ANSWER_D);
    }

    @Test
    @Transactional
    void fullUpdateTestQuestionWithPatch() throws Exception {
        // Initialize the database
        testQuestionRepository.saveAndFlush(testQuestion);

        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();

        // Update the testQuestion using partial update
        TestQuestion partialUpdatedTestQuestion = new TestQuestion();
        partialUpdatedTestQuestion.setId(testQuestion.getId());

        partialUpdatedTestQuestion
            .name(UPDATED_NAME)
            .level(UPDATED_LEVEL)
            .answerA(UPDATED_ANSWER_A)
            .answerB(UPDATED_ANSWER_B)
            .answerC(UPDATED_ANSWER_C)
            .answerD(UPDATED_ANSWER_D);

        restTestQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestQuestion))
            )
            .andExpect(status().isOk());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
        TestQuestion testTestQuestion = testQuestionList.get(testQuestionList.size() - 1);
        assertThat(testTestQuestion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTestQuestion.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTestQuestion.getAnswerA()).isEqualTo(UPDATED_ANSWER_A);
        assertThat(testTestQuestion.getAnswerB()).isEqualTo(UPDATED_ANSWER_B);
        assertThat(testTestQuestion.getAnswerC()).isEqualTo(UPDATED_ANSWER_C);
        assertThat(testTestQuestion.getAnswerD()).isEqualTo(UPDATED_ANSWER_D);
    }

    @Test
    @Transactional
    void patchNonExistingTestQuestion() throws Exception {
        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();
        testQuestion.setId(count.incrementAndGet());

        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestQuestion() throws Exception {
        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();
        testQuestion.setId(count.incrementAndGet());

        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestQuestion() throws Exception {
        int databaseSizeBeforeUpdate = testQuestionRepository.findAll().size();
        testQuestion.setId(count.incrementAndGet());

        // Create the TestQuestion
        TestQuestionDTO testQuestionDTO = testQuestionMapper.toDto(testQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testQuestionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestQuestion in the database
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestQuestion() throws Exception {
        // Initialize the database
        testQuestionRepository.saveAndFlush(testQuestion);

        int databaseSizeBeforeDelete = testQuestionRepository.findAll().size();

        // Delete the testQuestion
        restTestQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, testQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestQuestion> testQuestionList = testQuestionRepository.findAll();
        assertThat(testQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
