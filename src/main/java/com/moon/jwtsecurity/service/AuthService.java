package com.moon.jwtsecurity.service;

import com.moon.jwtsecurity.controller.dto.AuthRequest;
import com.moon.jwtsecurity.controller.dto.AuthResponse;
import com.moon.jwtsecurity.exception.ValidationException;
import com.moon.jwtsecurity.model.Role;
import com.moon.jwtsecurity.model.User;
import com.moon.jwtsecurity.repository.RoleRepository;
import com.moon.jwtsecurity.repository.UserRepository;
import com.moon.jwtsecurity.security.JwtTokenUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    AuthenticationManager authManager;

    public AuthResponse login(AuthRequest request) {
        String email = request.getEmail();
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    email, request.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTokenUtil.generateAccessToken(user);

            AuthResponse response = new AuthResponse();
            response.setEmail(email);
            response.setAccessToken(accessToken);
            return response;

        } catch (Exception e) {
            throw new ValidationException("Invalid username or password");
        }
    }

    public User userRegistration(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userFromDb != null) {
            throw new ValidationException("User already registered");
        }
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setUsername(user.getUsername());
        user.setPassword(encodedPassword);
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if (StringUtils.isNotEmpty(user.getEmail())) {
            String message = String.format(
                "Hello, you have registered on the The Best website \n" +
                    "Please visit next link: http://localhost:9000/api/auth/activate/%s",
                user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation Code", message);
        }

        return user;
    }

    public String activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return "Activation failed";
        }
        user.setActivationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        return "User successfully activated";
    }
}