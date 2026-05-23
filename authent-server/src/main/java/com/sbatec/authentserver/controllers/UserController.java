package com.sbatec.authentserver.controllers;

import com.sbatec.authentserver.config.CustomUserDetailsService;
import com.sbatec.authentserver.config.JwtService;
import com.sbatec.authentserver.dtos.AuthRequest;
import com.sbatec.authentserver.dtos.AuthResponse;
import com.sbatec.authentserver.dtos.RefreshRequest;
import com.sbatec.authentserver.dtos.User;
import com.sbatec.authentserver.services.externals.CompanyRestClient;
import com.sbatec.authentserver.services.internals.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    UserService userService;
    AuthenticationManager authManager;
    CustomUserDetailsService customUserDetailsService;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    CompanyRestClient companyRestClient;

    // RoleApiService roleApiService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        AuthResponse authResponse = new AuthResponse();
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (userDetails != null) {
            User user = userService.findByUserName(request.getUsername());
            String accessToken = jwtService.generateAccessToken((UserDetails) auth.getPrincipal());
            String refreshToken = jwtService.generateRefreshToken((UserDetails) auth.getPrincipal());
            //Company company = companyRestClient.findBySiret(user.getSiret());
            //authResponse.setCompany(company);
            authResponse.setUser(user);
            authResponse.setRefreshToken(refreshToken);
            authResponse.setAccessToken(accessToken);
        }
        return ResponseEntity.ok(authResponse);
    }

    /**
     *
     * @param request
     * @return
     */

    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        UserDetails user = customUserDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtService.generateAccessToken(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(newAccessToken);
        authResponse.setRefreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        System.out.println();
        return ResponseEntity.ok(null);
    }


    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/{userName:.+}")
    public User findByUserName(@PathVariable @NotNull String userName) {
        log.info("Get user by Email : {}", userName);
        return userService.findByUserName(userName);
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping()
    public List<User> findAllUsers() {
        log.info("Get all users by Email");
        List<User> users = userService.findAllUsers();

        if (users != null && !users.isEmpty()) {
            users.forEach(u -> {
                u.setPassword("");
            });
        }
        return users;
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/{email:.+}/{password}")
    public User findByUserNameAndPassword(@PathVariable @NotNull String email,
                                          @PathVariable @NotNull String password) {
        log.info("Get user by Email and password : {}", email);
        return userService.findByUserNameAndPassword(email, password);

    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/add")
    public User addUser(@RequestBody @NotNull User user) {
        log.info("Add user : {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.addUser(user);
    }


    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PutMapping(value = "/edit")
    public User editUser(@RequestBody @NotNull User user) {
        log.info("Add user : {}", user.getEmail());
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            User savedUser = userService.findUserById(user.getId());
            user.setPassword(savedUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userService.addUser(user);
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable @NotNull long id) {
        log.info("delete user by id : {}", id);
        userService.deleteUserById(id);
    }

    @Secured(value = {"ADMIN", "WRITE", "READ"})
    @GetMapping(value = "/getUserLogged")
    public ResponseEntity<Map<String, Object>> getLoggedUser(HttpServletRequest httpServletRequest) {
        Map<String, Object> params = new HashMap<>();
        HttpSession httpSession = httpServletRequest.getSession();
        SecurityContext security =
                (SecurityContext) httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (security != null && security.getAuthentication() != null) {
            String userName = security.getAuthentication().getName();

            List<String> roles = new ArrayList<>();
            for (GrantedAuthority ga : security.getAuthentication().getAuthorities()) {
                roles.add(ga.getAuthority());
            }
            params.put("username", userName);
            params.put("roles", roles);
        }
        if (params.isEmpty()) {
            return new ResponseEntity<>(null);
        }
        return null;
    }
}
