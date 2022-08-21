package com.moon.jwtsecurity.controller;

import com.moon.jwtsecurity.controller.dto.UserDTO;
import com.moon.jwtsecurity.mapper.UserMapper;
import com.moon.jwtsecurity.model.User;
import com.moon.jwtsecurity.service.UserService;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Api(tags = "Users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<UserDTO> findAllUsers() {
        log.info("getUsers - get all users");
        return userService.findAllUsers().stream().map(UserMapper.INSTANCE::toUserDtoFromUser)
            .toList();
    }

    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable Long id) {
        log.info("findUserById - get user with id: {}", id);
        User user = userService.findUserById(id);
        return UserMapper.INSTANCE.toUserDtoFromUser(user);
    }
}
