package com.reminder.repository;

import com.reminder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String login, String email);
    Optional<User> findByUsername(String login);
    Boolean existsByUsername(String login);
    Boolean existsByEmail(String email);

    String Username(String login);
}
