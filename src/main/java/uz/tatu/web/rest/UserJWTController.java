package uz.tatu.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import uz.tatu.security.jwt.JWTFilter;
import uz.tatu.security.jwt.TokenProvider;
import uz.tatu.web.rest.vm.LoginVM;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final Environment environment;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, UserDetailsService userDetailsService,  Environment environment, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.environment = environment;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        try {
//            boolean isActiveDirectoryCheck = environment.getProperty("spring.ldap.auth.enable", Boolean.class, false);
//            if (isActiveDirectoryCheck) {
//                boolean isValid = authenticationCheck(loginVM.getUsername(), loginVM.getPassword());
//                if (!isValid) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(loginVM.getUsername());
//
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    boolean rememberMe = (loginVM.isRememberMe() != null && loginVM.isRememberMe());
//                    String jwt = tokenProvider.createToken(authenticationToken, rememberMe);
//                    HttpHeaders httpHeaders = new HttpHeaders();
//                    httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//                    return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
//                }
//                UserDetails userDetails = userDetailsService.loadUserByUsername(loginVM.getUsername());
//
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                boolean rememberMe = loginVM.isRememberMe() != null && loginVM.isRememberMe();
//                String jwt = tokenProvider.createToken(authenticationToken, rememberMe);
//                HttpHeaders httpHeaders = new HttpHeaders();
//                httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//                return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
//            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
            Authentication authentication = null;

            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = loginVM.isRememberMe() != null && loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.ok(new JWTToken("Username or Password invalid"));
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
