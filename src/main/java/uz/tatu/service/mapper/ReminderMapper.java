package uz.tatu.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.Reminder;
import uz.tatu.service.dto.ReminderDTO;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReminderMapper extends EntityMapper<ReminderDTO, Reminder>{


    @Mapping(source = "createdBy.id", target = "createdById")
    ReminderDTO toDto(Reminder s);


    @Mapping(source = "createdById", target = "createdBy")
    Reminder toEntity(ReminderDTO s);

    default Reminder fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reminder reminder = new Reminder();
        reminder.setId(id);
        return reminder;
    }
}
