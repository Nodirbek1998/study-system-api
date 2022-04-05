package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.StudyLogs;
import uz.tatu.domain.User;
import uz.tatu.service.dto.StudyLogsDTO;

/**
 * Mapper for the entity {@link StudyLogs} and its DTO {@link StudyLogsDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface StudyLogsMapper extends EntityMapper<StudyLogsDTO, StudyLogs> {

    @Mapping(source = "users.id", target = "userId")
    StudyLogsDTO toDto(StudyLogs s);

    @Mapping(source = "userId", target = "users")
    StudyLogs toEntity(StudyLogsDTO s);

    default StudyLogs fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudyLogs studyLogs = new StudyLogs();
        studyLogs.setId(id);
        return studyLogs;
    }
}
