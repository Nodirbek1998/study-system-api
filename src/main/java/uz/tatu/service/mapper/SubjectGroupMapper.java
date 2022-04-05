package uz.tatu.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.SubjectGroup;
import uz.tatu.service.dto.SubjectGroupDto;

@Mapper(componentModel = "spring", uses = {SubjectsMapper.class, GroupsMapper.class})
public interface SubjectGroupMapper extends EntityMapper<SubjectGroupDto, SubjectGroup> {


    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "group.id", target = "groupId")
    SubjectGroupDto toDto(SubjectGroup s);

    @Mapping(source = "subjectId", target = "subject")
    @Mapping(source = "groupId", target = "group")
    SubjectGroup toEntity(SubjectGroupDto s);

    default SubjectGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        SubjectGroup subjectGroup = new SubjectGroup();
        subjectGroup.setId(id);
        return subjectGroup;
    }
}
