package uz.tatu.web.rest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import liquibase.util.StringUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.tatu.config.ApplicationProperties;
import uz.tatu.domain.User;
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.repository.ImagesRepository;
import uz.tatu.service.ImagesService;
import uz.tatu.service.UserService;
import uz.tatu.service.dto.FilesDTO;
import uz.tatu.service.dto.ImagesDTO;
import uz.tatu.service.dto.UserDTO;
import uz.tatu.service.utils.DateUtils;
import uz.tatu.service.utils.FileHelper;
import uz.tatu.service.utils.ImageUtils;
import uz.tatu.web.rest.errors.BadRequestAlertException;

import javax.imageio.ImageIO;

/**
 * REST controller for managing {@link uz.tatu.domain.Images}.
 */
@RestController
@RequestMapping("/api")
public class ImagesResource {

    private final Logger log = LoggerFactory.getLogger(ImagesResource.class);

    private static final String ENTITY_NAME = "images";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagesService imagesService;

    private final ImagesRepository imagesRepository;
    private final MessageSource messageSource;
    private final MultipartProperties multipartProperties;
    private final ApplicationProperties applicationSettingDto;
    private final UserService userService;

    public ImagesResource(ImagesService imagesService, ImagesRepository imagesRepository, MessageSource messageSource, MultipartProperties multipartProperties, ApplicationProperties applicationSettingDto, UserService userService) {
        this.imagesService = imagesService;
        this.imagesRepository = imagesRepository;
        this.messageSource = messageSource;
        this.multipartProperties = multipartProperties;
        this.applicationSettingDto = applicationSettingDto;
        this.userService = userService;
    }

    /**
     * {@code POST  /images} : Create a new images.
     *
     * @param imagesDTO the imagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagesDTO, or with status {@code 400 (Bad Request)} if the images has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/images")
    public ResponseEntity<ImagesDTO> createImages(@RequestBody ImagesDTO imagesDTO) throws URISyntaxException {
        log.debug("REST request to save Images : {}", imagesDTO);
        if (imagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new images cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImagesDTO result = imagesService.save(imagesDTO);
        return ResponseEntity
            .created(new URI("/api/images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /images/:id} : Updates an existing images.
     *
     * @param id the id of the imagesDTO to save.
     * @param imagesDTO the imagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagesDTO,
     * or with status {@code 400 (Bad Request)} if the imagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/images/{id}")
    public ResponseEntity<ImagesDTO> updateImages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImagesDTO imagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Images : {}, {}", id, imagesDTO);
        if (imagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImagesDTO result = imagesService.save(imagesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /images/:id} : Partial updates given fields of an existing images, field will ignore if it is null
     *
     * @param id the id of the imagesDTO to save.
     * @param imagesDTO the imagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagesDTO,
     * or with status {@code 400 (Bad Request)} if the imagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImagesDTO> partialUpdateImages(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImagesDTO imagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Images partially : {}, {}", id, imagesDTO);
        if (imagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImagesDTO> result = imagesService.partialUpdate(imagesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /images} : get all the images.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images in body.
     */
    @GetMapping("/images")
    public ResponseEntity<List<ImagesDTO>> getAllImages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Images");
        Page<ImagesDTO> page = imagesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /images/:id} : get the "id" images.
     *
     * @param userid the id of the imagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagesDTO, or with status {@code 404 (Not Found)}.
     */

    @GetMapping(value = "/images-by-user/{userid}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] imageUserDownload(@PathVariable Long userid) throws IOException {
        Optional<UserDTO> userDTO = userService.findOne(userid);
        if (userDTO.isPresent()) {
            Optional<ImagesDTO> imagesDTO = imagesService.findOne(userDTO.get().getImageUrl() != null ? Long.parseLong(userDTO.get().getImageUrl()) : 0);
            if (imagesDTO.isPresent()) {
                ImagesDTO file = imagesDTO.get();
                String fileformat = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                String rootPath = applicationSettingDto.getApplicationSettingDto().getFilepath();
                String rootFilePath = FileHelper.getUploadFilePath(rootPath + FileHelper.getPhotoDirectory(), file.getCreatedAt());
                Path target = Paths.get(rootFilePath, file.getId() + "_thumb.png");
                if (target.toFile().exists()) {
                    InputStream in = new FileInputStream(target.toFile());
                    return IOUtils.toByteArray(in);
                } else {
                    rootFilePath = FileHelper.getUploadFilePath(rootPath + FileHelper.getFilesDirectory(), file.getCreatedAt());
                    target = Paths.get(rootFilePath, file.getId() + "_thumb.png");
                    if (target.toFile().exists()) {
                        InputStream in = new FileInputStream(target.toFile());
                        return IOUtils.toByteArray(in);
                    }
                }
            }
        }
        return null;
    }

    @PostMapping("/images-upload")
    public ResponseEntity<ImagesDTO> uploadFiles(@RequestParam("file") MultipartFile file,
                                                @RequestParam(value = "name", required = false) String name) throws Exception {
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
        if (file.getSize() > multipartProperties.getMaxFileSize().toBytes()) {
            throw new BadRequestAlertException(messageSource.getMessage("file.validation.max.file.size", io.jsonwebtoken.lang.Objects.toObjectArray(5), LocaleContextHolder.getLocale()), ENTITY_NAME, "file error");
        }
        filePath = FileHelper.getUploadFilePath(this.applicationSettingDto.getApplicationSettingDto().getFilepath() + FileHelper.getPhotoDirectory(), currentdate);

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!StringUtils.isEmpty(fileExtension) && fileExtension.equalsIgnoreCase("exe")) {
            throw new BadRequestAlertException(messageSource.getMessage("file.validation.format.not.valid", io.jsonwebtoken.lang.Objects.toObjectArray(fileExtension), LocaleContextHolder.getLocale()), ENTITY_NAME, "fileFormat");
        }
        String fileName = Stream.of(name, file.getOriginalFilename()).filter(java.util.Objects::nonNull).findFirst().orElse(null);
        ImagesDTO result;

        result = imagesService.saveImage(file, currentUser.get(), filePath);

        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image.getWidth() < 200 || image.getHeight() < 200) {
            ImageIO.write(image, "png", new File(filePath + result.getId() + "_thumb.png"));
        } else {
            BufferedImage scaled = ImageUtils.getScaledInstance(image, 200, 200, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
            ImageIO.write(scaled, "png", new File(filePath + result.getId() + "_thumb.png"));
        }


        return ResponseEntity.created(new URI("/api/files/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /images/:id} : get the "id" images.
     *
     * @param imagesId the id of the imagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagesDTO, or with status {@code 404 (Not Found)}.
     */

    @GetMapping(value = "/images/{imagesId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] imageDownload(@PathVariable Long imagesId) throws IOException {
        Optional<ImagesDTO> imagesDTO = imagesService.findOne(imagesId);
        if (imagesDTO.isPresent()) {
            ImagesDTO file = imagesDTO.get();
            String fileformat = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            String rootPath = applicationSettingDto.getApplicationSettingDto().getFilepath();
            String rootFilePath = FileHelper.getUploadFilePath(rootPath + FileHelper.getPhotoDirectory(), file.getCreatedAt());
            Path target = Paths.get(rootFilePath, file.getId() + "_thumb.png");
            if (target.toFile().exists()) {
                InputStream in = new FileInputStream(target.toFile());
                return IOUtils.toByteArray(in);
            } else {
                rootFilePath = FileHelper.getUploadFilePath(rootPath + FileHelper.getFilesDirectory(), file.getCreatedAt());
                target = Paths.get(rootFilePath, file.getId() + "_thumb.png");
                if (target.toFile().exists()) {
                    InputStream in = new FileInputStream(target.toFile());
                    return IOUtils.toByteArray(in);
                }
            }
        }
        return null;
    }

    /**
     * {@code DELETE  /images/:id} : delete the "id" images.
     *
     * @param id the id of the imagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImages(@PathVariable Long id) {
        log.debug("REST request to delete Images : {}", id);
        imagesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
