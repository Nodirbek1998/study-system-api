package uz.tatu.web.rest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.tatu.config.ApplicationProperties;
import liquibase.util.StringUtils;
import uz.tatu.domain.User;
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.repository.FilesRepository;
import uz.tatu.service.FilesService;
import uz.tatu.service.UserService;
import uz.tatu.service.dto.FilesDTO;
import uz.tatu.service.dto.TaskDTO;
import uz.tatu.service.utils.DateUtils;
import uz.tatu.service.utils.FileHelper;
import uz.tatu.service.utils.ImageUtils;
import uz.tatu.web.rest.errors.BadRequestAlertException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing {@link uz.tatu.domain.Files}.
 */
@RestController
@RequestMapping("/api")
public class FilesResource {

    private final Logger log = LoggerFactory.getLogger(FilesResource.class);

    private static final String ENTITY_NAME = "files";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationProperties applicationSettingDto;
    private final FilesService filesService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final MultipartProperties multipartProperties;

    private final FilesRepository filesRepository;

    public FilesResource(ApplicationProperties applicationSettingDto,
                         FilesService filesService, UserService userService,
                         MessageSource messageSource, MultipartProperties multipartProperties,
                         FilesRepository filesRepository) {
        this.applicationSettingDto = applicationSettingDto;
        this.filesService = filesService;
        this.userService = userService;
        this.messageSource = messageSource;
        this.multipartProperties = multipartProperties;
        this.filesRepository = filesRepository;
    }

    /**
     * {@code POST  /files} : Create a new files.
     *
     * @param filesDTO the filesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filesDTO, or with status {@code 400 (Bad Request)} if the files has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/files")
    public ResponseEntity<FilesDTO> createFiles(@RequestBody FilesDTO filesDTO) throws URISyntaxException {
        log.debug("REST request to save Files : {}", filesDTO);
        if (filesDTO.getId() != null) {
            throw new BadRequestAlertException("A new files cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilesDTO result = filesService.save(filesDTO);
        return ResponseEntity
            .created(new URI("/api/files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /files/:id} : Updates an existing files.
     *
     * @param id the id of the filesDTO to save.
     * @param filesDTO the filesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filesDTO,
     * or with status {@code 400 (Bad Request)} if the filesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/files/{id}")
    public ResponseEntity<FilesDTO> updateFiles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilesDTO filesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Files : {}, {}", id, filesDTO);
        if (filesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilesDTO result = filesService.save(filesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filesDTO.getId().toString()))
            .body(result);
    }

    @PostMapping("/files-upload")
    public ResponseEntity<FilesDTO> uploadFiles(@RequestParam("type") int type,
                                                   @RequestParam("file") MultipartFile file) throws Exception {
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (!userService.getAccessMethod(EnumStaticPermission.EdoFilesAdd, currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        log.debug("REST request to save EdoFiles : {}", file.getName());
        LocalDate currentdate = DateUtils.getSysLocaleDate();
        if (file.getSize() == 0) {
            throw new BadRequestAlertException(messageSource.getMessage("file.validation.not.available", null, LocaleContextHolder.getLocale()), ENTITY_NAME, "file");
        }
        String filePath = "";
        if (type == 1) {    //userlarni rasmlarini saqlash uchun
            if (file.getSize() > multipartProperties.getMaxFileSize().toBytes()) {
                throw new BadRequestAlertException(messageSource.getMessage("file.validation.max.file.size", io.jsonwebtoken.lang.Objects.toObjectArray(5), LocaleContextHolder.getLocale()), ENTITY_NAME, "file error");
            }
            filePath = FileHelper.getUploadFilePath(this.applicationSettingDto.getApplicationSettingDto().getFilepath() + FileHelper.getPhotoDirectory(), currentdate);

        } else {    // qolgan upload qilinadigan hamma fayllar
            if (file.getSize() > 200 * 1024 * 1024) {
                throw new BadRequestAlertException(messageSource.getMessage("file.validation.max.file.size", io.jsonwebtoken.lang.Objects.toObjectArray(100), LocaleContextHolder.getLocale()), ENTITY_NAME, "file error");
            }
            filePath = FileHelper.getUploadFilePath(this.applicationSettingDto.getApplicationSettingDto().getFilepath() + FileHelper.getFilesDirectory(), currentdate);
        }

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!StringUtils.isEmpty(fileExtension) && fileExtension.equalsIgnoreCase("exe")) {
            throw new BadRequestAlertException(messageSource.getMessage("file.validation.format.not.valid", io.jsonwebtoken.lang.Objects.toObjectArray(fileExtension), LocaleContextHolder.getLocale()), ENTITY_NAME, "fileFormat");
        }

        FilesDTO result;
        if (type == 3 && filesService.isDocFile(fileExtension)) {
            result = filesService.saveFileDocFile(file, currentUser.get(), filePath);
        } else {
            result = filesService.saveFileAllFile(file, currentUser.get(), filePath);
        }

        if (type == 1) {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image.getWidth() < 200 || image.getHeight() < 200) {
                ImageIO.write(image, "png", new File(filePath + result.getId() + "_thumb.png"));
            } else {
                BufferedImage scaled = ImageUtils.getScaledInstance(image, 200, 200, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
                ImageIO.write(scaled, "png", new File(filePath + result.getId() + "_thumb.png"));
            }
        }


        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /files/:id} : Partial updates given fields of an existing files, field will ignore if it is null
     *
     * @param id the id of the filesDTO to save.
     * @param filesDTO the filesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filesDTO,
     * or with status {@code 400 (Bad Request)} if the filesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/files/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FilesDTO> partialUpdateFiles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilesDTO filesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Files partially : {}, {}", id, filesDTO);
        if (filesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilesDTO> result = filesService.partialUpdate(filesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /files} : get all the files.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files in body.
     */
    @GetMapping("/files")
    public ResponseEntity<List<FilesDTO>> getAllFiles(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Files");
        Page<FilesDTO> page = filesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /files/:id} : get the "id" files.
     *
     * @param id the id of the filesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/files/{id}")
    public ResponseEntity<FilesDTO> getFiles(@PathVariable Long id) {
        log.debug("REST request to get Files : {}", id);
        Optional<FilesDTO> filesDTO = filesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filesDTO);
    }

    /**
     * {@code DELETE  /files/:id} : delete the "id" files.
     *
     * @param id the id of the filesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFiles(@PathVariable Long id) {
        log.debug("REST request to delete Files : {}", id);
        filesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/edo-files-pdf/{id}")
    public ResponseEntity<InputStreamResource> getPdfEdoInternalApprovals(@PathVariable Long id, HttpServletRequest request) throws IOException {
        log.debug("REST request to get content OgProjectFiles : {}", id);
        ByteArrayInputStream pdf = null;
        Optional<FilesDTO> filesDTO = filesService.findOne(id);
        if (filesDTO.isPresent()) {
            if(filesDTO.isPresent()){
                String rootFilePath = applicationSettingDto.getApplicationSettingDto().getFilepath();
                rootFilePath = FileHelper.getUploadFilePath(rootFilePath + FileHelper.getFilesDirectory(), filesDTO.get().getCreatedAt());

                Path target = Paths.get(rootFilePath, filesDTO.get().getId() + "." + FilenameUtils.getExtension(filesDTO.get().getName()));
                if (target.toFile().exists()) {
                    pdf = new ByteArrayInputStream(FileUtils.readFileToByteArray(target.toFile()));
                }
            }
        }



        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "inline; filename=" + id+".pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.set("X-Frame-Options", "SAMEORIGIN");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
