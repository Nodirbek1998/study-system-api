package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.StudyLogs;
import uz.tatu.service.dto.StudyLogsDTO;

/**
 * Mapper for the entity {@link StudyLogs} and its DTO {@link StudyLogsDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudyUsersMapper.class })
public interface StudyLogsMapper extends EntityMapper<StudyLogsDTO, StudyLogs> {
    @Mapping(target = "studyUser", source = "studyUser", qualifiedByName = "id")
    StudyLogsDTO toDto(StudyLogs s);
}
