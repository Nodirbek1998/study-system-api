package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Images;
import uz.tatu.service.dto.ImagesDTO;

/**
 * Mapper for the entity {@link Images} and its DTO {@link ImagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagesMapper extends EntityMapper<ImagesDTO, Images> {

    ImagesDTO toDto(Images s);

    Images toEntity(ImagesDTO s);

    default Images fromId(Long id) {
        if (id == null) {
            return null;
        }
        Images images = new Images();
        images.setId(id);
        return images;
    }

}
