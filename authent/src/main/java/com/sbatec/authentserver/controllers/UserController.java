package com.sbatec.authentserver.controllers;

import com.sbatec.authentserver.config.JwtService;
import com.sbatec.authentserver.dtos.*;
import com.sbatec.authentserver.services.internals.RoleService;
import com.sbatec.authentserver.services.internals.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@Validated
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    RoleService roleService;
    AuthenticationManager authManager;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;


    @PostMapping({"/login", "/"})
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.info("Authentification userName : {}", request.getUsername());
        AuthResponse authResponse = new AuthResponse();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword());
        Authentication auth = authManager.authenticate(
                usernamePasswordAuthenticationToken
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (userDetails != null) {
            User user = userService.findByUserName(request.getUsername());
            String accessToken = jwtService.generateAccessToken(request.getUsername());
            String refreshToken = jwtService.generateRefreshToken(request.getUsername());
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

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();


        AuthResponse authResponse = new AuthResponse();
        //authResponse.setAccessToken(newAccessToken);
        authResponse.setRefreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/users")
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

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/{userName:.+}")
    public User findByUserName(@PathVariable @NotNull String userName) {
        log.info("Get user by Email : {}", userName);
        return userService.findByUserName(userName);
    }


    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(value = "/{email:.+}/{password}")
    public User findByUserNameAndPassword(@PathVariable @NotNull String email,
                                          @PathVariable @NotNull String password) {
        log.info("Get user by Email and password : {}", email);
        return userService.findByUserNameAndPassword(email, password);

    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/add")
    public User addUser(@RequestBody @NotNull User user) {
        log.info("Add user : {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.addUser(user);
    }

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

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable @NotNull long id) {
        log.info("delete user by id : {}", id);
        userService.deleteUserById(id);
    }


    @Secured(value = {"ROLE_ADMIN", "ROLE_WRITE", "ROLE_READ"})
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        log.info("Get all role ref");
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }
}
