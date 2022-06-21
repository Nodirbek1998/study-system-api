package uz.tatu.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.Examples;
import uz.tatu.service.dto.ExamplesDTO;

@Mapper(componentModel = "spring", uses = {UserMapper.class, FilesMapper.class})
public interface ExamplesMapper extends EntityMapper<ExamplesDTO, Examples> {

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "files.id", target = "filesId")
    ExamplesDTO toDto(Examples s);

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "filesId", target = "files")
    Examples toEntity(ExamplesDTO s);

    default Examples fromId(Long id) {
        if (id == null) {
            return null;
        }
        Examples examples = new Examples();
        examples.setId(id);
        return examples;
    }
}
