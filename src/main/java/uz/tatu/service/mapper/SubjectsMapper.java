package uz.tatu.service.mapper;

import org.mapstruct.*;
import uz.tatu.domain.Subjects;
import uz.tatu.service.dto.SubjectsDTO;

/**
 * Mapper for the entity {@link Subjects} and its DTO {@link SubjectsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubjectsMapper extends EntityMapper<SubjectsDTO, Subjects> {

    default Subjects fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subjects subjects = new Subjects();
        subjects.setId(id);
        return subjects;

    }
}
