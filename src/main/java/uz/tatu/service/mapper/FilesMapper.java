package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Files;
import uz.tatu.service.dto.FilesDTO;

/**
 * Mapper for the entity {@link Files} and its DTO {@link FilesDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface FilesMapper extends EntityMapper<FilesDTO, Files> {

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdAt", target = "createdAt")
    FilesDTO toDto(Files s);

    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "createdAt", target = "createdAt")
    Files toDto(FilesDTO s);

    default Files fromId(Long id) {
        if (id == null) {
            return null;
        }
        Files files = new Files();
        files.setId(id);
        return files;
    }
}
