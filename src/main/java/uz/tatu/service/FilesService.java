package uz.tatu.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.tatu.domain.User;
import uz.tatu.service.dto.FilesDTO;

/**
 * Service Interface for managing {@link uz.tatu.domain.Files}.
 */
public interface FilesService {
    /**
     * Save a files.
     *
     * @param filesDTO the entity to save.
     * @return the persisted entity.
     */
    FilesDTO save(FilesDTO filesDTO);

    /**
     * Partially updates a files.
     *
     * @param filesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FilesDTO> partialUpdate(FilesDTO filesDTO);

    /**
     * Get all the files.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FilesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" files.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FilesDTO> findOne(Long id);

    /**
     * Delete the "id" files.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean isDocFile(String fileExtension);

    FilesDTO saveFileDocFile(MultipartFile file, User user, String filePath);

    FilesDTO saveFileAllFile(MultipartFile file, User user, String filePath) throws IOException;
}
