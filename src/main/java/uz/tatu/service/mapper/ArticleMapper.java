package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Article;
import uz.tatu.service.dto.ArticleDTO;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {

    @Mapping(source = "users.id", target = "usersId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "updatedBy.id", target = "updatedById")
    ArticleDTO toDto(Article s);

    @Mapping(source = "usersId", target = "users")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "updatedById", target = "updatedBy")
    Article toEntity(ArticleDTO s);

    default Article fromId(Long id) {
        if (id == null) {
            return null;
        }
        Article article = new Article();
        article.setId(id);
        return article;
    }
}
