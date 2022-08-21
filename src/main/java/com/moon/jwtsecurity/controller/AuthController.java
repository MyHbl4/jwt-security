package com.moon.jwtsecurity.controller;

import com.moon.jwtsecurity.controller.dto.AuthRequest;
import com.moon.jwtsecurity.controller.dto.AuthResponse;
import com.moon.jwtsecurity.controller.dto.MessageResponse;
import com.moon.jwtsecurity.controller.dto.UserDTO;
import com.moon.jwtsecurity.controller.dto.UserNewDTO;
import com.moon.jwtsecurity.mapper.UserMapper;
import com.moon.jwtsecurity.model.User;
import com.moon.jwtsecurity.service.AuthService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Api(tags = "Authentication")
public class AuthController {

    @Autowired
    AuthService authService;

    @PutMapping("/register")
    public UserDTO createUser(@RequestBody @Valid UserNewDTO user) {
        log.info("createUser - User created with email: {}", user.getEmail());
        User createdUser = authService.userRegistration(
            UserMapper.INSTANCE.toUserFromUserNewDto(user));
        return UserMapper.INSTANCE.toUserDtoFromUser(createdUser);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest requestDto) {
        log.info("login - login user with email: {}", requestDto.getEmail());
        return authService.login(requestDto);
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        log.info("activate - activate user account: {}", code);
        String response = authService.activateUser(code);
        return ResponseEntity.ok().body(new MessageResponse(response));
    }
}
