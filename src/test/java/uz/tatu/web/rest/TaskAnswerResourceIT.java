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
import uz.tatu.domain.TaskAnswer;
import uz.tatu.repository.TaskAnswerRepository;
import uz.tatu.service.TaskAnswerService;
import uz.tatu.service.dto.TaskAnswerDTO;
import uz.tatu.service.mapper.TaskAnswerMapper;

/**
 * Integration tests for the {@link TaskAnswerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaskAnswerResourceIT {

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/task-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskAnswerRepository taskAnswerRepository;

    @Mock
    private TaskAnswerRepository taskAnswerRepositoryMock;

    @Autowired
    private TaskAnswerMapper taskAnswerMapper;

    @Mock
    private TaskAnswerService taskAnswerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskAnswerMockMvc;

    private TaskAnswer taskAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskAnswer createEntity(EntityManager em) {
        TaskAnswer taskAnswer = new TaskAnswer().createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        return taskAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskAnswer createUpdatedEntity(EntityManager em) {
        TaskAnswer taskAnswer = new TaskAnswer().createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        return taskAnswer;
    }

    @BeforeEach
    public void initTest() {
        taskAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void createTaskAnswer() throws Exception {
        int databaseSizeBeforeCreate = taskAnswerRepository.findAll().size();
        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);
        restTaskAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO)))
            .andExpect(status().isCreated());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        TaskAnswer testTaskAnswer = taskAnswerList.get(taskAnswerList.size() - 1);
        assertThat(testTaskAnswer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTaskAnswer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTaskAnswerWithExistingId() throws Exception {
        // Create the TaskAnswer with an existing ID
        taskAnswer.setId(1L);
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        int databaseSizeBeforeCreate = taskAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskAnswerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaskAnswers() throws Exception {
        // Initialize the database
        taskAnswerRepository.saveAndFlush(taskAnswer);

        // Get all the taskAnswerList
        restTaskAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaskAnswersWithEagerRelationshipsIsEnabled() throws Exception {
        when(taskAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaskAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taskAnswerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaskAnswersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taskAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaskAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taskAnswerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTaskAnswer() throws Exception {
        // Initialize the database
        taskAnswerRepository.saveAndFlush(taskAnswer);

        // Get the taskAnswer
        restTaskAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, taskAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskAnswer.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTaskAnswer() throws Exception {
        // Get the taskAnswer
        restTaskAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaskAnswer() throws Exception {
        // Initialize the database
        taskAnswerRepository.saveAndFlush(taskAnswer);

        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();

        // Update the taskAnswer
        TaskAnswer updatedTaskAnswer = taskAnswerRepository.findById(taskAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedTaskAnswer are not directly saved in db
        em.detach(updatedTaskAnswer);
        updatedTaskAnswer.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(updatedTaskAnswer);

        restTaskAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
        TaskAnswer testTaskAnswer = taskAnswerList.get(taskAnswerList.size() - 1);
        assertThat(testTaskAnswer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaskAnswer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTaskAnswer() throws Exception {
        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();
        taskAnswer.setId(count.incrementAndGet());

        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskAnswerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskAnswer() throws Exception {
        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();
        taskAnswer.setId(count.incrementAndGet());

        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskAnswer() throws Exception {
        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();
        taskAnswer.setId(count.incrementAndGet());

        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAnswerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskAnswerWithPatch() throws Exception {
        // Initialize the database
        taskAnswerRepository.saveAndFlush(taskAnswer);

        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();

        // Update the taskAnswer using partial update
        TaskAnswer partialUpdatedTaskAnswer = new TaskAnswer();
        partialUpdatedTaskAnswer.setId(taskAnswer.getId());

        partialUpdatedTaskAnswer.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restTaskAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
        TaskAnswer testTaskAnswer = taskAnswerList.get(taskAnswerList.size() - 1);
        assertThat(testTaskAnswer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaskAnswer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTaskAnswerWithPatch() throws Exception {
        // Initialize the database
        taskAnswerRepository.saveAndFlush(taskAnswer);

        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();

        // Update the taskAnswer using partial update
        TaskAnswer partialUpdatedTaskAnswer = new TaskAnswer();
        partialUpdatedTaskAnswer.setId(taskAnswer.getId());

        partialUpdatedTaskAnswer.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restTaskAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskAnswer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaskAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
        TaskAnswer testTaskAnswer = taskAnswerList.get(taskAnswerList.size() - 1);
        assertThat(testTaskAnswer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaskAnswer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTaskAnswer() throws Exception {
        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();
        taskAnswer.setId(count.incrementAndGet());

        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskAnswerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskAnswer() throws Exception {
        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();
        taskAnswer.setId(count.incrementAndGet());

        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskAnswer() throws Exception {
        int databaseSizeBeforeUpdate = taskAnswerRepository.findAll().size();
        taskAnswer.setId(count.incrementAndGet());

        // Create the TaskAnswer
        TaskAnswerDTO taskAnswerDTO = taskAnswerMapper.toDto(taskAnswer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskAnswer in the database
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskAnswer() throws Exception {
        // Initialize the database
        taskAnswerRepository.saveAndFlush(taskAnswer);

        int databaseSizeBeforeDelete = taskAnswerRepository.findAll().size();

        // Delete the taskAnswer
        restTaskAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskAnswer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskAnswer> taskAnswerList = taskAnswerRepository.findAll();
        assertThat(taskAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
