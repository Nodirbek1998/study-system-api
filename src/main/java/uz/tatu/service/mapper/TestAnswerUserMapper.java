package uz.tatu.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.tatu.domain.TestAnswerUser;
import uz.tatu.service.dto.TestAnswerUserDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TestAnswerMapper.class})
public interface TestAnswerUserMapper extends EntityMapper<TestAnswerUserDto, TestAnswerUser> {

    @Mapping(source = "users.id", target = "userId")
    @Mapping(source = "testAnswer.id", target = "testAnswerId")
    TestAnswerUserDto toDto(TestAnswerUser s);

    @Mapping(source = "userId", target = "users")
    @Mapping(source = "testAnswerId", target = "testAnswer")
    TestAnswerUser toEntity(TestAnswerUserDto s);

    default TestAnswerUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestAnswerUser testAnswerUser = new TestAnswerUser();
        testAnswerUser.setId(id);
        return testAnswerUser;
    }
}
