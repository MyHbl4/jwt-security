package com.moon.jwtsecurity.service;

import com.moon.jwtsecurity.exception.NotFoundException;
import com.moon.jwtsecurity.model.User;
import com.moon.jwtsecurity.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User with id " + id + " does not exist."));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
