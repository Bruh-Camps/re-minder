package com.reminder.service;

import com.reminder.model.User;
import com.reminder.repository.UserRepository;
import com.reminder.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(loginOrEmail, loginOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + loginOrEmail));

        // Adapta para usar CustomUserDetails, que lida com authorities (papéis)
        return new CustomUserDetails(user);
    }
}