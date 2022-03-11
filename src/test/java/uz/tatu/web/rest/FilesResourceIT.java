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
import uz.tatu.domain.Files;
import uz.tatu.repository.FilesRepository;
import uz.tatu.service.dto.FilesDTO;
import uz.tatu.service.mapper.FilesMapper;

/**
 * Integration tests for the {@link FilesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_FILE_SIZE = 1D;
    private static final Double UPDATED_FILE_SIZE = 2D;

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private FilesMapper filesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilesMockMvc;

    private Files files;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Files createEntity(EntityManager em) {
        Files files = new Files()
            .name(DEFAULT_NAME)
            .fileSize(DEFAULT_FILE_SIZE)
            .contentType(DEFAULT_CONTENT_TYPE)
            .createdAt(DEFAULT_CREATED_AT);
        return files;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Files createUpdatedEntity(EntityManager em) {
        Files files = new Files()
            .name(UPDATED_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .contentType(UPDATED_CONTENT_TYPE)
            .createdAt(UPDATED_CREATED_AT);
        return files;
    }

    @BeforeEach
    public void initTest() {
        files = createEntity(em);
    }

    @Test
    @Transactional
    void createFiles() throws Exception {
        int databaseSizeBeforeCreate = filesRepository.findAll().size();
        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);
        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isCreated());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeCreate + 1);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFiles.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testFiles.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testFiles.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createFilesWithExistingId() throws Exception {
        // Create the Files with an existing ID
        files.setId(1L);
        FilesDTO filesDTO = filesMapper.toDto(files);

        int databaseSizeBeforeCreate = filesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get all the filesList
        restFilesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(files.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        // Get the files
        restFilesMockMvc
            .perform(get(ENTITY_API_URL_ID, files.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(files.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.doubleValue()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFiles() throws Exception {
        // Get the files
        restFilesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeUpdate = filesRepository.findAll().size();

        // Update the files
        Files updatedFiles = filesRepository.findById(files.getId()).get();
        // Disconnect from session so that the updates on updatedFiles are not directly saved in db
        em.detach(updatedFiles);
        updatedFiles.name(UPDATED_NAME).fileSize(UPDATED_FILE_SIZE).contentType(UPDATED_CONTENT_TYPE).createdAt(UPDATED_CREATED_AT);
        FilesDTO filesDTO = filesMapper.toDto(updatedFiles);

        restFilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiles.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFiles.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFiles.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(count.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(count.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(count.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilesWithPatch() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeUpdate = filesRepository.findAll().size();

        // Update the files using partial update
        Files partialUpdatedFiles = new Files();
        partialUpdatedFiles.setId(files.getId());

        partialUpdatedFiles.name(UPDATED_NAME).fileSize(UPDATED_FILE_SIZE).createdAt(UPDATED_CREATED_AT);

        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiles))
            )
            .andExpect(status().isOk());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiles.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFiles.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testFiles.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateFilesWithPatch() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeUpdate = filesRepository.findAll().size();

        // Update the files using partial update
        Files partialUpdatedFiles = new Files();
        partialUpdatedFiles.setId(files.getId());

        partialUpdatedFiles.name(UPDATED_NAME).fileSize(UPDATED_FILE_SIZE).contentType(UPDATED_CONTENT_TYPE).createdAt(UPDATED_CREATED_AT);

        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiles))
            )
            .andExpect(status().isOk());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
        Files testFiles = filesList.get(filesList.size() - 1);
        assertThat(testFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiles.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFiles.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFiles.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(count.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(count.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiles() throws Exception {
        int databaseSizeBeforeUpdate = filesRepository.findAll().size();
        files.setId(count.incrementAndGet());

        // Create the Files
        FilesDTO filesDTO = filesMapper.toDto(files);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Files in the database
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFiles() throws Exception {
        // Initialize the database
        filesRepository.saveAndFlush(files);

        int databaseSizeBeforeDelete = filesRepository.findAll().size();

        // Delete the files
        restFilesMockMvc
            .perform(delete(ENTITY_API_URL_ID, files.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Files> filesList = filesRepository.findAll();
        assertThat(filesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
