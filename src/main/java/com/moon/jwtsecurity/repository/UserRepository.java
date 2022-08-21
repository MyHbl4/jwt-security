package com.moon.jwtsecurity.repository;

import com.moon.jwtsecurity.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findUserByUsername(String username);

    User findByActivationCode(String code);
}
