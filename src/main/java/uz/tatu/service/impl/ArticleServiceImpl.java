package uz.tatu.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.Article;
import uz.tatu.domain.Images;
import uz.tatu.repository.ArticleRepository;
import uz.tatu.repository.ImagesRepository;
import uz.tatu.repository.impl.ArticleRepositoryImpl;
import uz.tatu.service.ArticleService;
import uz.tatu.service.custom.ArticleListDTO;
import uz.tatu.service.dto.ArticleDTO;
import uz.tatu.service.mapper.ArticleMapper;
import uz.tatu.service.mapper.ImagesMapper;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;
    private final ImagesRepository imagesRepository;
    private final ImagesMapper imagesMapper;
    private final ArticleRepositoryImpl articleRepositoryImpl;

    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper, ImagesRepository imagesRepository, ImagesMapper imagesMapper, ArticleRepositoryImpl articleRepositoryImpl) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
        this.articleRepositoryImpl = articleRepositoryImpl;
    }

    @Override
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Images images = new Images();
        Article article = articleMapper.toEntity(articleDTO);
        if (articleDTO.getImagesDTO().getId() != null){
            images = imagesRepository.save(imagesMapper.toEntity(articleDTO.getImagesDTO()));
        }
        article = articleRepository.save(article);
        article.setImages(images);
        return articleMapper.toDto(article);
    }

    @Override
    public Optional<ArticleDTO> partialUpdate(ArticleDTO articleDTO) {
        log.debug("Request to partially update Article : {}", articleDTO);

        return articleRepository
            .findById(articleDTO.getId())
            .map(existingArticle -> {
                articleMapper.partialUpdate(existingArticle, articleDTO);

                return existingArticle;
            })
            .map(articleRepository::save)
            .map(articleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleListDTO> findAll(MultiValueMap<String , String> queryParam, Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepositoryImpl.findAll(queryParam, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        Optional<Article> article = articleRepository.findById(id);
        Optional<ArticleDTO> articleDTO = article.map(articleMapper::toDto);
        if (articleDTO.isPresent() && article.isPresent() && article.get().getImages() != null){
            articleDTO.get().setImagesDTO(imagesMapper.toDto(article.get().getImages()));
        }
        return articleDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }
}
