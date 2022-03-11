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
import uz.tatu.domain.StudyUsers;
import uz.tatu.repository.StudyUsersRepository;
import uz.tatu.service.dto.StudyUsersDTO;
import uz.tatu.service.mapper.StudyUsersMapper;

/**
 * Integration tests for the {@link StudyUsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudyUsersResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/study-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudyUsersRepository studyUsersRepository;

    @Autowired
    private StudyUsersMapper studyUsersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudyUsersMockMvc;

    private StudyUsers studyUsers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyUsers createEntity(EntityManager em) {
        StudyUsers studyUsers = new StudyUsers()
            .fullName(DEFAULT_FULL_NAME)
            .age(DEFAULT_AGE)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return studyUsers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyUsers createUpdatedEntity(EntityManager em) {
        StudyUsers studyUsers = new StudyUsers()
            .fullName(UPDATED_FULL_NAME)
            .age(UPDATED_AGE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return studyUsers;
    }

    @BeforeEach
    public void initTest() {
        studyUsers = createEntity(em);
    }

    @Test
    @Transactional
    void createStudyUsers() throws Exception {
        int databaseSizeBeforeCreate = studyUsersRepository.findAll().size();
        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);
        restStudyUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyUsersDTO)))
            .andExpect(status().isCreated());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeCreate + 1);
        StudyUsers testStudyUsers = studyUsersList.get(studyUsersList.size() - 1);
        assertThat(testStudyUsers.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testStudyUsers.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testStudyUsers.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testStudyUsers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudyUsers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testStudyUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testStudyUsers.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStudyUsers.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createStudyUsersWithExistingId() throws Exception {
        // Create the StudyUsers with an existing ID
        studyUsers.setId(1L);
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        int databaseSizeBeforeCreate = studyUsersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyUsersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudyUsers() throws Exception {
        // Initialize the database
        studyUsersRepository.saveAndFlush(studyUsers);

        // Get all the studyUsersList
        restStudyUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyUsers.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getStudyUsers() throws Exception {
        // Initialize the database
        studyUsersRepository.saveAndFlush(studyUsers);

        // Get the studyUsers
        restStudyUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, studyUsers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studyUsers.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStudyUsers() throws Exception {
        // Get the studyUsers
        restStudyUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudyUsers() throws Exception {
        // Initialize the database
        studyUsersRepository.saveAndFlush(studyUsers);

        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();

        // Update the studyUsers
        StudyUsers updatedStudyUsers = studyUsersRepository.findById(studyUsers.getId()).get();
        // Disconnect from session so that the updates on updatedStudyUsers are not directly saved in db
        em.detach(updatedStudyUsers);
        updatedStudyUsers
            .fullName(UPDATED_FULL_NAME)
            .age(UPDATED_AGE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(updatedStudyUsers);

        restStudyUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyUsersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyUsersDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
        StudyUsers testStudyUsers = studyUsersList.get(studyUsersList.size() - 1);
        assertThat(testStudyUsers.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testStudyUsers.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testStudyUsers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testStudyUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudyUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testStudyUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testStudyUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudyUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingStudyUsers() throws Exception {
        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();
        studyUsers.setId(count.incrementAndGet());

        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyUsersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudyUsers() throws Exception {
        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();
        studyUsers.setId(count.incrementAndGet());

        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudyUsers() throws Exception {
        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();
        studyUsers.setId(count.incrementAndGet());

        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyUsersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudyUsersWithPatch() throws Exception {
        // Initialize the database
        studyUsersRepository.saveAndFlush(studyUsers);

        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();

        // Update the studyUsers using partial update
        StudyUsers partialUpdatedStudyUsers = new StudyUsers();
        partialUpdatedStudyUsers.setId(studyUsers.getId());

        partialUpdatedStudyUsers.phone(UPDATED_PHONE).email(UPDATED_EMAIL).username(UPDATED_USERNAME).updatedAt(UPDATED_UPDATED_AT);

        restStudyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudyUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudyUsers))
            )
            .andExpect(status().isOk());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
        StudyUsers testStudyUsers = studyUsersList.get(studyUsersList.size() - 1);
        assertThat(testStudyUsers.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testStudyUsers.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testStudyUsers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testStudyUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudyUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testStudyUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testStudyUsers.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStudyUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateStudyUsersWithPatch() throws Exception {
        // Initialize the database
        studyUsersRepository.saveAndFlush(studyUsers);

        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();

        // Update the studyUsers using partial update
        StudyUsers partialUpdatedStudyUsers = new StudyUsers();
        partialUpdatedStudyUsers.setId(studyUsers.getId());

        partialUpdatedStudyUsers
            .fullName(UPDATED_FULL_NAME)
            .age(UPDATED_AGE)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restStudyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudyUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudyUsers))
            )
            .andExpect(status().isOk());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
        StudyUsers testStudyUsers = studyUsersList.get(studyUsersList.size() - 1);
        assertThat(testStudyUsers.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testStudyUsers.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testStudyUsers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testStudyUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudyUsers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testStudyUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testStudyUsers.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudyUsers.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingStudyUsers() throws Exception {
        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();
        studyUsers.setId(count.incrementAndGet());

        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studyUsersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudyUsers() throws Exception {
        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();
        studyUsers.setId(count.incrementAndGet());

        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyUsersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudyUsers() throws Exception {
        int databaseSizeBeforeUpdate = studyUsersRepository.findAll().size();
        studyUsers.setId(count.incrementAndGet());

        // Create the StudyUsers
        StudyUsersDTO studyUsersDTO = studyUsersMapper.toDto(studyUsers);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyUsersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studyUsersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudyUsers in the database
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudyUsers() throws Exception {
        // Initialize the database
        studyUsersRepository.saveAndFlush(studyUsers);

        int databaseSizeBeforeDelete = studyUsersRepository.findAll().size();

        // Delete the studyUsers
        restStudyUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, studyUsers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudyUsers> studyUsersList = studyUsersRepository.findAll();
        assertThat(studyUsersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
