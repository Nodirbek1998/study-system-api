package uz.tatu.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.ExamplesAnswer;
import uz.tatu.service.dto.ExamplesAnswerDTO;

@Mapper(componentModel = "spring", uses = {UserMapper.class, FilesMapper.class, ExamplesMapper.class})
public interface ExamplesAnswerMapper extends EntityMapper<ExamplesAnswerDTO, ExamplesAnswer> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "filesId", target = "files")
    @Mapping(source = "examplesId", target = "examples")
    ExamplesAnswer toEntity(ExamplesAnswerDTO dto);


    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(source = "files.id", target = "filesId")
    @Mapping(source = "examples.id", target = "examplesId")
    ExamplesAnswerDTO toDto(ExamplesAnswer entity);
}
