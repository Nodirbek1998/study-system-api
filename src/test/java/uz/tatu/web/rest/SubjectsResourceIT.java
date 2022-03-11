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
import uz.tatu.domain.Subjects;
import uz.tatu.repository.SubjectsRepository;
import uz.tatu.service.dto.SubjectsDTO;
import uz.tatu.service.mapper.SubjectsMapper;

/**
 * Integration tests for the {@link SubjectsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubjectsResourceIT {

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

    private static final String ENTITY_API_URL = "/api/subjects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private SubjectsMapper subjectsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectsMockMvc;

    private Subjects subjects;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subjects createEntity(EntityManager em) {
        Subjects subjects = new Subjects()
            .nameUz(DEFAULT_NAME_UZ)
            .nameRu(DEFAULT_NAME_RU)
            .nameEn(DEFAULT_NAME_EN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return subjects;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subjects createUpdatedEntity(EntityManager em) {
        Subjects subjects = new Subjects()
            .nameUz(UPDATED_NAME_UZ)
            .nameRu(UPDATED_NAME_RU)
            .nameEn(UPDATED_NAME_EN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return subjects;
    }

    @BeforeEach
    public void initTest() {
        subjects = createEntity(em);
    }

    @Test
    @Transactional
    void createSubjects() throws Exception {
        int databaseSizeBeforeCreate = subjectsRepository.findAll().size();
        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);
        restSubjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectsDTO)))
            .andExpect(status().isCreated());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeCreate + 1);
        Subjects testSubjects = subjectsList.get(subjectsList.size() - 1);
        assertThat(testSubjects.getNameUz()).isEqualTo(DEFAULT_NAME_UZ);
        assertThat(testSubjects.getNameRu()).isEqualTo(DEFAULT_NAME_RU);
        assertThat(testSubjects.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testSubjects.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubjects.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSubjectsWithExistingId() throws Exception {
        // Create the Subjects with an existing ID
        subjects.setId(1L);
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        int databaseSizeBeforeCreate = subjectsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubjects() throws Exception {
        // Initialize the database
        subjectsRepository.saveAndFlush(subjects);

        // Get all the subjectsList
        restSubjectsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subjects.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameUz").value(hasItem(DEFAULT_NAME_UZ)))
            .andExpect(jsonPath("$.[*].nameRu").value(hasItem(DEFAULT_NAME_RU)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getSubjects() throws Exception {
        // Initialize the database
        subjectsRepository.saveAndFlush(subjects);

        // Get the subjects
        restSubjectsMockMvc
            .perform(get(ENTITY_API_URL_ID, subjects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subjects.getId().intValue()))
            .andExpect(jsonPath("$.nameUz").value(DEFAULT_NAME_UZ))
            .andExpect(jsonPath("$.nameRu").value(DEFAULT_NAME_RU))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubjects() throws Exception {
        // Get the subjects
        restSubjectsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubjects() throws Exception {
        // Initialize the database
        subjectsRepository.saveAndFlush(subjects);

        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();

        // Update the subjects
        Subjects updatedSubjects = subjectsRepository.findById(subjects.getId()).get();
        // Disconnect from session so that the updates on updatedSubjects are not directly saved in db
        em.detach(updatedSubjects);
        updatedSubjects
            .nameUz(UPDATED_NAME_UZ)
            .nameRu(UPDATED_NAME_RU)
            .nameEn(UPDATED_NAME_EN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(updatedSubjects);

        restSubjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
        Subjects testSubjects = subjectsList.get(subjectsList.size() - 1);
        assertThat(testSubjects.getNameUz()).isEqualTo(UPDATED_NAME_UZ);
        assertThat(testSubjects.getNameRu()).isEqualTo(UPDATED_NAME_RU);
        assertThat(testSubjects.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testSubjects.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubjects.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSubjects() throws Exception {
        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();
        subjects.setId(count.incrementAndGet());

        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subjectsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubjects() throws Exception {
        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();
        subjects.setId(count.incrementAndGet());

        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subjectsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubjects() throws Exception {
        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();
        subjects.setId(count.incrementAndGet());

        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subjectsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubjectsWithPatch() throws Exception {
        // Initialize the database
        subjectsRepository.saveAndFlush(subjects);

        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();

        // Update the subjects using partial update
        Subjects partialUpdatedSubjects = new Subjects();
        partialUpdatedSubjects.setId(subjects.getId());

        partialUpdatedSubjects.nameUz(UPDATED_NAME_UZ).nameRu(UPDATED_NAME_RU).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restSubjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjects))
            )
            .andExpect(status().isOk());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
        Subjects testSubjects = subjectsList.get(subjectsList.size() - 1);
        assertThat(testSubjects.getNameUz()).isEqualTo(UPDATED_NAME_UZ);
        assertThat(testSubjects.getNameRu()).isEqualTo(UPDATED_NAME_RU);
        assertThat(testSubjects.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testSubjects.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubjects.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSubjectsWithPatch() throws Exception {
        // Initialize the database
        subjectsRepository.saveAndFlush(subjects);

        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();

        // Update the subjects using partial update
        Subjects partialUpdatedSubjects = new Subjects();
        partialUpdatedSubjects.setId(subjects.getId());

        partialUpdatedSubjects
            .nameUz(UPDATED_NAME_UZ)
            .nameRu(UPDATED_NAME_RU)
            .nameEn(UPDATED_NAME_EN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSubjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubjects))
            )
            .andExpect(status().isOk());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
        Subjects testSubjects = subjectsList.get(subjectsList.size() - 1);
        assertThat(testSubjects.getNameUz()).isEqualTo(UPDATED_NAME_UZ);
        assertThat(testSubjects.getNameRu()).isEqualTo(UPDATED_NAME_RU);
        assertThat(testSubjects.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testSubjects.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubjects.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSubjects() throws Exception {
        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();
        subjects.setId(count.incrementAndGet());

        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subjectsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubjects() throws Exception {
        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();
        subjects.setId(count.incrementAndGet());

        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subjectsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubjects() throws Exception {
        int databaseSizeBeforeUpdate = subjectsRepository.findAll().size();
        subjects.setId(count.incrementAndGet());

        // Create the Subjects
        SubjectsDTO subjectsDTO = subjectsMapper.toDto(subjects);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subjectsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subjects in the database
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubjects() throws Exception {
        // Initialize the database
        subjectsRepository.saveAndFlush(subjects);

        int databaseSizeBeforeDelete = subjectsRepository.findAll().size();

        // Delete the subjects
        restSubjectsMockMvc
            .perform(delete(ENTITY_API_URL_ID, subjects.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subjects> subjectsList = subjectsRepository.findAll();
        assertThat(subjectsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
