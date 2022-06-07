package uz.tatu.service.impl;

import java.io.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;
import uz.tatu.domain.Images;
import uz.tatu.domain.User;
import uz.tatu.repository.ImagesRepository;
import uz.tatu.service.ImagesService;
import uz.tatu.service.dto.FilesDTO;
import uz.tatu.service.dto.ImagesDTO;
import uz.tatu.service.mapper.ImagesMapper;

/**
 * Service Implementation for managing {@link Images}.
 */
@Service
@Transactional
public class ImagesServiceImpl implements ImagesService {

    private final Logger log = LoggerFactory.getLogger(ImagesServiceImpl.class);

    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    public ImagesServiceImpl(ImagesRepository imagesRepository, ImagesMapper imagesMapper) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
    }

    @Override
    public ImagesDTO save(ImagesDTO imagesDTO) {
        log.debug("Request to save Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        return imagesMapper.toDto(images);
    }

    @Override
    public Optional<ImagesDTO> partialUpdate(ImagesDTO imagesDTO) {
        log.debug("Request to partially update Images : {}", imagesDTO);

        return imagesRepository
            .findById(imagesDTO.getId())
            .map(existingImages -> {
                imagesMapper.partialUpdate(existingImages, imagesDTO);

                return existingImages;
            })
            .map(imagesRepository::save)
            .map(imagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imagesRepository.findAll(pageable).map(imagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImagesDTO> findOne(Long id) {
        log.debug("Request to get Images : {}", id);
        return imagesRepository.findById(id).map(imagesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Images : {}", id);
        imagesRepository.deleteById(id);
    }

    @Override
    public ImagesDTO saveImage(MultipartFile file, User user, String filePath) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        ImagesDTO imagesDTO = new ImagesDTO();
        imagesDTO.setContentType(file.getContentType());
        imagesDTO.setImageSize(file.getSize());
        imagesDTO.setName(file.getOriginalFilename());
        imagesDTO.setCreatedBy(user.getId());
        ImagesDTO result = save(imagesDTO);

        File afile = new File(filePath + result.getId() + "." + fileExtension);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(afile));
        stream.write(file.getBytes());
        stream.close();
        return result;

    }
}
