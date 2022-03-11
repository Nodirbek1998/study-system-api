package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Article;
import uz.tatu.service.dto.ArticleDTO;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class })
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {
    @Mapping(target = "studyUser", source = "studyUser", qualifiedByName = "id")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "id")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "id")
    ArticleDTO toDto(Article s);
}
