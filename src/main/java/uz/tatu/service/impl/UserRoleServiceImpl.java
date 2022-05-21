package uz.tatu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import uz.tatu.domain.Role;
import uz.tatu.domain.User;
import uz.tatu.domain.UserRole;
import uz.tatu.repository.RoleRepository;
import uz.tatu.repository.UserRepository;
import uz.tatu.repository.UserRoleRepository;
import uz.tatu.service.UserRoleService;
import uz.tatu.service.UsernameAlreadyUsedException;
import uz.tatu.service.dto.UserRoleDto;
import uz.tatu.service.mapper.UserRoleMapper;
import uz.tatu.service.utils.StringFilterUtil;
import uz.tatu.web.rest.errors.BadRequestAlertException;

import javax.persistence.criteria.Join;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final Logger log = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private final UserRoleMapper userRoleMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private final CacheManager cacheManager;

    public UserRoleServiceImpl(UserRoleMapper userRoleMapper, RoleRepository roleRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, MessageSource messageSource, PasswordEncoder passwordEncoder, CacheManager cacheManager) {
        this.userRoleMapper = userRoleMapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
        this.cacheManager = cacheManager;
    }

    @Override
    public UserRoleDto save(UserRoleDto userRoleDto, User user) {
        log.debug("Request to save EdoUserRole : {}", userRoleDto);
        UserRole userRole = userRoleMapper.toEntity(userRoleDto);
        Optional<Role> edoDtRoles = roleRepository.findById(userRoleDto.getRoleId());
        if (!edoDtRoles.isPresent()) {
            //TODO So`rash kerak
            throw new BadRequestAlertException("RoleId not found", "EdoUserRole", "HtmlText");
        }


            User users = userRepository.findById(userRoleDto.getUserId()).get();
            List<UserRole> roleList = userRoleRepository.findByUser_Id(userRoleDto.getUserId());
            if (roleList.size() > 0) {
                userRole.setId(roleList.get(0).getId());
            }
            userRole = userRoleRepository.save(userRole);

            if (userRoleDto.getLogin() != null) {

                Optional<User> userByUsername = userRepository.findOneByLogin(userRoleDto.getLogin());

                if (userByUsername.isPresent() && !userByUsername.get().getId().equals(userRoleDto.getUserId())) {
                    throw new BadRequestAlertException(
                            String.format("%s. %s : ",
                                    messageSource.getMessage("user.exceptions.username.exist", null, LocaleContextHolder.getLocale()),
                                    messageSource.getMessage("common.texts.user", null, LocaleContextHolder.getLocale())) +
                                    userByUsername.get().getFirstName(),
                            "EdoUserRole",
                            messageSource.getMessage("common.texts.user", null, LocaleContextHolder.getLocale())+" : " + userByUsername.get().getFirstName());
                }

                if (!userRoleDto.getLogin().equals("")) {
                    try {

                        userRepository.findOneByLogin(userRoleDto.getLogin()).ifPresent(existingUser -> {
                            if (existingUser.getId() != users.getId()) {
                                throw new UsernameAlreadyUsedException();
                            }
                        });

                        if (StringUtils.isEmpty(userRoleDto.getPassword())) {
                            if (users.getPassword() == null || users.getPassword().equals("")) {
                                String encryptedPassword = passwordEncoder.encode("123456");
                                users.setPassword(encryptedPassword);
                            }
                        } else {
                            String encryptedPassword = passwordEncoder.encode(userRoleDto.getPassword());
                            users.setPassword(encryptedPassword);
                        }

                        users.setLogin(userRoleDto.getLogin());
                        users.setActivated(true);
                        users.setLangKey("ru");


                        userRepository.save(users);
                        /*xozircha yopib quydim chatga xato berayotgani uchun*/
                        //edoUsersService.registerChat(edoUserRoleDTO.getUserId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            this.clearUserCaches(users);

        }
        return userRoleMapper.toDto(userRole);
    }

    @Override
    public Page<UserRoleDto> findAll(Pageable pageable, MultiValueMap<String, String> queryParams) {
        log.debug("Request to get all UserRoles");
        Long roleId = 0L;
        Specification<UserRole> specification = Specification.where(null);
        if (queryParams != null) {
            if (queryParams.getFirst("id") != null) {
                specification = specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), queryParams.getFirst("id")));
            }

            if (queryParams.getFirst("nameEn") != null) {
                specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
                    Join<UserRole, Role> columnJoin = root.join("role");
                    return criteriaBuilder.like(columnJoin.get("nameEn"), StringFilterUtil.strLikeTwoSide(queryParams.getFirst("nameEn")));
                });
            }

            if (queryParams.getFirst("roleId") != null) {
                specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
                    Join<UserRole, Role> columnJoin = root.join("role");
                    return criteriaBuilder.equal(columnJoin.get("id"), queryParams.getFirst("roleId"));
                });
            }

            if (queryParams.getFirst("userId") != null) {
                specification = specification.and((root, criteriaQuery, criteriaBuilder) -> {
                    Join<UserRole, User> columnJoin = root.join("user");
                    return criteriaBuilder.equal(columnJoin.get("id"), queryParams.getFirst("userId"));
                });
            }
        }
        return userRoleRepository.findAll(specification, pageable)
                .map(userRoleMapper::toDto);
    }

    private void clearUserCaches(User user) {
         Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRoleRepository.USERS_ROLE_BY_ID_CACHE)).evict(user.getId());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
