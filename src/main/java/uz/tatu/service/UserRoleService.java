package uz.tatu.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import uz.tatu.domain.User;
import uz.tatu.service.dto.UserRoleDto;

public interface UserRoleService {


    UserRoleDto save(UserRoleDto edoUserRoleDTO, User user);

    Page<UserRoleDto> findAll(Pageable pageable, MultiValueMap<String, String> queryParams);
}
