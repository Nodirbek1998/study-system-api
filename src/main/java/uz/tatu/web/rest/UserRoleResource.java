package uz.tatu.web.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import uz.tatu.domain.User;
import uz.tatu.domain.enumeration.EnumStaticPermission;
import uz.tatu.service.UserRoleService;
import uz.tatu.service.UserService;
import uz.tatu.service.dto.UserRoleDto;
import uz.tatu.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserRoleResource.class);

    private static final String ENTITY_NAME = "UserRole";

    private final MessageSource messageSource;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UserRoleResource(MessageSource messageSource, UserService userService, UserRoleService userRoleService) {
        this.messageSource = messageSource;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }


    @PostMapping("/user-roles")
    public ResponseEntity<UserRoleDto> createEdoUserRole(@RequestBody UserRoleDto edoUserRoleDTO) throws URISyntaxException {
        log.debug("REST request to save EdoUserRole : {}", edoUserRoleDTO);
        if(edoUserRoleDTO.getId() != null) {
            throw new BadRequestAlertException(messageSource.getMessage("common.exceptions.validation.exist.id",
                    new String[]{messageSource.getMessage("common.tables." + ENTITY_NAME, null, LocaleContextHolder.getLocale())},
                    LocaleContextHolder.getLocale()), ENTITY_NAME, "idexists");
        }
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if(!userService.getAccessMethodForProduction(EnumStaticPermission.UserRoleAdd, currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        if(edoUserRoleDTO.getRoleId() == null  && !userService.getAccessMethodForProduction(EnumStaticPermission.EdoDtRoleStaticPermissionAdd, currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        UserRoleDto result = userRoleService.save(edoUserRoleDTO, currentUser.get());
        return ResponseEntity.created(new URI("/api/user-roles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping("/user-roles")
    public ResponseEntity<List<UserRoleDto>> getAllUserRoles(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams) {
        log.debug("REST request to get a page of EdoUserRoles");
        if(!userService.getAccessMethod (EnumStaticPermission.EdoUserRoleView)){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }
        Page<UserRoleDto> page = userRoleService.findAll(pageable, queryParams);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
