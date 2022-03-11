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
import uz.tatu.domain.StudyLogs;
import uz.tatu.domain.enumeration.EnumActionType;
import uz.tatu.repository.StudyLogsRepository;
import uz.tatu.service.dto.StudyLogsDTO;
import uz.tatu.service.mapper.StudyLogsMapper;

/**
 * Integration tests for the {@link StudyLogsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudyLogsResourceIT {

    private static final String DEFAULT_OPERATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_IP = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_IP = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final EnumActionType DEFAULT_ACTION_TYPE = EnumActionType.Login;
    private static final EnumActionType UPDATED_ACTION_TYPE = EnumActionType.Logout;

    private static final String ENTITY_API_URL = "/api/study-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudyLogsRepository studyLogsRepository;

    @Autowired
    private StudyLogsMapper studyLogsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudyLogsMockMvc;

    private StudyLogs studyLogs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyLogs createEntity(EntityManager em) {
        StudyLogs studyLogs = new StudyLogs()
            .operationName(DEFAULT_OPERATION_NAME)
            .clientIp(DEFAULT_CLIENT_IP)
            .host(DEFAULT_HOST)
            .createdAt(DEFAULT_CREATED_AT)
            .actionType(DEFAULT_ACTION_TYPE);
        return studyLogs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyLogs createUpdatedEntity(EntityManager em) {
        StudyLogs studyLogs = new StudyLogs()
            .operationName(UPDATED_OPERATION_NAME)
            .clientIp(UPDATED_CLIENT_IP)
            .host(UPDATED_HOST)
            .createdAt(UPDATED_CREATED_AT)
            .actionType(UPDATED_ACTION_TYPE);
        return studyLogs;
    }

    @BeforeEach
    public void initTest() {
        studyLogs = createEntity(em);
    }

    @Test
    @Transactional
    void createStudyLogs() throws Exception {
        int databaseSizeBeforeCreate = studyLogsRepository.findAll().size();
        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);
        restStudyLogsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyLogsDTO)))
            .andExpect(status().isCreated());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeCreate + 1);
        StudyLogs testStudyLogs = studyLogsList.get(studyLogsList.size() - 1);
        assertThat(testStudyLogs.getOperationName()).isEqualTo(DEFAULT_OPERATION_NAME);
        assertThat(testStudyLogs.getClientIp()).isEqualTo(DEFAULT_CLIENT_IP);
        assertThat(testStudyLogs.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testStudyLogs.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStudyLogs.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
    }

    @Test
    @Transactional
    void createStudyLogsWithExistingId() throws Exception {
        // Create the StudyLogs with an existing ID
        studyLogs.setId(1L);
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        int databaseSizeBeforeCreate = studyLogsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyLogsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyLogsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudyLogs() throws Exception {
        // Initialize the database
        studyLogsRepository.saveAndFlush(studyLogs);

        // Get all the studyLogsList
        restStudyLogsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyLogs.getId().intValue())))
            .andExpect(jsonPath("$.[*].operationName").value(hasItem(DEFAULT_OPERATION_NAME)))
            .andExpect(jsonPath("$.[*].clientIp").value(hasItem(DEFAULT_CLIENT_IP)))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getStudyLogs() throws Exception {
        // Initialize the database
        studyLogsRepository.saveAndFlush(studyLogs);

        // Get the studyLogs
        restStudyLogsMockMvc
            .perform(get(ENTITY_API_URL_ID, studyLogs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studyLogs.getId().intValue()))
            .andExpect(jsonPath("$.operationName").value(DEFAULT_OPERATION_NAME))
            .andExpect(jsonPath("$.clientIp").value(DEFAULT_CLIENT_IP))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStudyLogs() throws Exception {
        // Get the studyLogs
        restStudyLogsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudyLogs() throws Exception {
        // Initialize the database
        studyLogsRepository.saveAndFlush(studyLogs);

        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();

        // Update the studyLogs
        StudyLogs updatedStudyLogs = studyLogsRepository.findById(studyLogs.getId()).get();
        // Disconnect from session so that the updates on updatedStudyLogs are not directly saved in db
        em.detach(updatedStudyLogs);
        updatedStudyLogs
            .operationName(UPDATED_OPERATION_NAME)
            .clientIp(UPDATED_CLIENT_IP)
            .host(UPDATED_HOST)
            .createdAt(UPDATED_CREATED_AT)
            .actionType(UPDATED_ACTION_TYPE);
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(updatedStudyLogs);

        restStudyLogsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyLogsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyLogsDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
        StudyLogs testStudyLogs = studyLogsList.get(studyLogsList.size() - 1);
        assertThat(testStudyLogs.getOperationName()).isEqualTo(UPDATED_OPERATION_NAME);
        assertThat(testStudyLogs.getClientIp()).isEqualTo(UPDATED_CLIENT_IP);
        assertThat(testStudyLogs.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testStudyLogs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudyLogs.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingStudyLogs() throws Exception {
        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();
        studyLogs.setId(count.incrementAndGet());

        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyLogsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyLogsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyLogsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudyLogs() throws Exception {
        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();
        studyLogs.setId(count.incrementAndGet());

        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyLogsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyLogsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudyLogs() throws Exception {
        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();
        studyLogs.setId(count.incrementAndGet());

        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyLogsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyLogsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudyLogsWithPatch() throws Exception {
        // Initialize the database
        studyLogsRepository.saveAndFlush(studyLogs);

        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();

        // Update the studyLogs using partial update
        StudyLogs partialUpdatedStudyLogs = new StudyLogs();
        partialUpdatedStudyLogs.setId(studyLogs.getId());

        partialUpdatedStudyLogs.host(UPDATED_HOST).createdAt(UPDATED_CREATED_AT);

        restStudyLogsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudyLogs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudyLogs))
            )
            .andExpect(status().isOk());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
        StudyLogs testStudyLogs = studyLogsList.get(studyLogsList.size() - 1);
        assertThat(testStudyLogs.getOperationName()).isEqualTo(DEFAULT_OPERATION_NAME);
        assertThat(testStudyLogs.getClientIp()).isEqualTo(DEFAULT_CLIENT_IP);
        assertThat(testStudyLogs.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testStudyLogs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudyLogs.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateStudyLogsWithPatch() throws Exception {
        // Initialize the database
        studyLogsRepository.saveAndFlush(studyLogs);

        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();

        // Update the studyLogs using partial update
        StudyLogs partialUpdatedStudyLogs = new StudyLogs();
        partialUpdatedStudyLogs.setId(studyLogs.getId());

        partialUpdatedStudyLogs
            .operationName(UPDATED_OPERATION_NAME)
            .clientIp(UPDATED_CLIENT_IP)
            .host(UPDATED_HOST)
            .createdAt(UPDATED_CREATED_AT)
            .actionType(UPDATED_ACTION_TYPE);

        restStudyLogsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudyLogs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudyLogs))
            )
            .andExpect(status().isOk());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
        StudyLogs testStudyLogs = studyLogsList.get(studyLogsList.size() - 1);
        assertThat(testStudyLogs.getOperationName()).isEqualTo(UPDATED_OPERATION_NAME);
        assertThat(testStudyLogs.getClientIp()).isEqualTo(UPDATED_CLIENT_IP);
        assertThat(testStudyLogs.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testStudyLogs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudyLogs.getActionType()).isEqualTo(UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingStudyLogs() throws Exception {
        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();
        studyLogs.setId(count.incrementAndGet());

        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyLogsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studyLogsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyLogsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudyLogs() throws Exception {
        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();
        studyLogs.setId(count.incrementAndGet());

        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyLogsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyLogsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudyLogs() throws Exception {
        int databaseSizeBeforeUpdate = studyLogsRepository.findAll().size();
        studyLogs.setId(count.incrementAndGet());

        // Create the StudyLogs
        StudyLogsDTO studyLogsDTO = studyLogsMapper.toDto(studyLogs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyLogsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studyLogsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudyLogs in the database
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudyLogs() throws Exception {
        // Initialize the database
        studyLogsRepository.saveAndFlush(studyLogs);

        int databaseSizeBeforeDelete = studyLogsRepository.findAll().size();

        // Delete the studyLogs
        restStudyLogsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studyLogs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudyLogs> studyLogsList = studyLogsRepository.findAll();
        assertThat(studyLogsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
