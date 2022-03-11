package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Files;
import uz.tatu.service.dto.FilesDTO;

/**
 * Mapper for the entity {@link Files} and its DTO {@link FilesDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class })
public interface FilesMapper extends EntityMapper<FilesDTO, Files> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "id")
    FilesDTO toDto(Files s);
}
