package com.reminder.service;

import com.reminder.model.User;
import com.reminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findByUsername(String login) {
        return userRepository.findByUsername(login);
    }

    // Outros métodos para salvar, atualizar e deletar usuários
}
