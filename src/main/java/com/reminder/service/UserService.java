package com.reminder.service;

import com.reminder.model.User;
import com.reminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public User getCurrentUser() {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String login = authentication.getName();
        if(login.equals("anonymousUser")){
            throw new RuntimeException("Usuário não autenticado.");
        }

        // Busca o usuário no banco
        Optional<User> user = this.findByUsername(login);
        if (user.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado no banco de dados.");
        }

        return user.get();
    }

}
