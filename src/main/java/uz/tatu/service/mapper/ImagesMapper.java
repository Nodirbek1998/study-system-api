package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Images;
import uz.tatu.service.dto.ImagesDTO;

/**
 * Mapper for the entity {@link Images} and its DTO {@link ImagesDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class })
public interface ImagesMapper extends EntityMapper<ImagesDTO, Images> {
    @Mapping(target = "studyUser", source = "studyUser", qualifiedByName = "id")
    ImagesDTO toDto(Images s);
}
