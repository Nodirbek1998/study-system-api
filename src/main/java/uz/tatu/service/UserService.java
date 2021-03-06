package uz.tatu.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;
import uz.tatu.config.Constants;
import uz.tatu.domain.*;
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.repository.*;
import uz.tatu.security.SecurityUtils;
import uz.tatu.service.dto.UserDTO;
import uz.tatu.service.mapper.UserMapper;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;
    
    private final RoleStaticPermissionRepository roleStaticPermissionRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserRoleRepository userRoleRepository, RoleRepository roleRepository, UserMapper userMapper, AuthorityRepository authorityRepository,
            CacheManager cacheManager,
            RoleStaticPermissionRepository roleStaticPermissionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.roleStaticPermissionRepository = roleStaticPermissionRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }
    @Transactional(readOnly = true)
    public boolean getAccessMethod(EnumStaticPermission permission) {
        Optional<User> currentUser = getUserWithAuthorities();
        AtomicBoolean result = new AtomicBoolean(false);
        currentUser.ifPresent(user->{
            List<EnumStaticPermission> staticPermissions = roleStaticPermissionRepository.findByRoles_Id(getDefaultRoleId(user.getAuthorities()))
                    .stream().map(RoleStaticPermission::getStaticPermission).collect(Collectors.toList());
            result.set(staticPermissions.stream().anyMatch(staticPermission -> staticPermission.equals(permission)));
        });
        return true;
//        return result.get();
    }

    @Transactional(readOnly = true)
    public boolean getAccessMethod(EnumStaticPermission permission, Optional<User> currentUser) {
        if(!currentUser.isPresent()){
            return false;
        }

        AtomicBoolean result = new AtomicBoolean(false);
        currentUser.ifPresent(user->{
            List<EnumStaticPermission> staticPermissions = roleStaticPermissionRepository.findByRoles_Id(getDefaultRoleId(user.getAuthorities()))
                    .stream().map(RoleStaticPermission::getStaticPermission).collect(Collectors.toList());
            result.set(staticPermissions.stream().anyMatch(staticPermission -> staticPermission.equals(permission)));
        });
        return true;
//        return result.get();
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setPhone(userDTO.getPhone());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode("1234");
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        this.clearUserCaches(user);

        if (userDTO.getAuthorities() != null && userDTO.getAuthorities() > 0L) {
            Optional<Role> roles = roleRepository.findById(userDTO.getAuthorities());
            roles.ifPresent(role->{
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                userRoleRepository.save(userRole);
                user.setAuthorities(role.getId());
            });
        }

        log.debug("Created Information for User: {}", user);
        return user;
    }

    @Transactional(readOnly = true)
    public boolean getAccessMethodForProduction(EnumStaticPermission permission, Optional<User> currentUser) {
        if(!currentUser.isPresent()){
            return false;
        }

        AtomicBoolean result = new AtomicBoolean(false);
        currentUser.ifPresent(user->{
            List<EnumStaticPermission> staticPermissions = roleStaticPermissionRepository.findByRoles_Id(getDefaultRoleId(user.getAuthorities()))
                    .stream().map(RoleStaticPermission::getStaticPermission).collect(Collectors.toList());
            result.set(staticPermissions.stream().anyMatch(staticPermission -> staticPermission.equals(permission)));
        });
        return result.get();
    }

    private Long getDefaultRoleId(Long roleId){
        if (roleId > 0) {
            return roleId;
        }
        return 1L;
    }
    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        Optional<UserDTO> adminUserDTO = Optional
                .of(userRepository.findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    this.clearUserCaches(user);
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    user.setAge(userDTO.getAge());
                    user.setPhone(userDTO.getPhone());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    if (userDTO.getAuthorities() != null && userDTO.getAuthorities() > 0L) {
                        userRoleRepository.deleteAllByUser_Id(user.getId());
                        Optional<Role> edoDtRoles = roleRepository.findById(userDTO.getAuthorities());
                        edoDtRoles.ifPresent(role -> {
                            UserRole userRole = new UserRole();
                            userRole.setRole(role);
                            userRole.setUser(user);
                            userRoleRepository.save(userRole);
                            user.setAuthorities(role.getId());
                        });
                    }
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(userMapper::toDto);
        return adminUserDTO;
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<User> getOne(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDTO> findOne(Long id){
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
//        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        return SecurityUtils.getCurrentUserLogin().flatMap(s -> {
            Optional<User> users = userRepository.findOneWithAuthoritiesByLogin(s);
            users.ifPresent(edoUsers -> {
                userRoleRepository.findFirstByUser_Id(edoUsers.getId()).ifPresent(row->{
                    edoUsers.setAuthorities(row.getRole().getId());
                });
            });
            return users;
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
