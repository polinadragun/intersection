package com.intersection.application.services.implementations;

import com.intersection.application.repositoryAbstractions.UserRepository;
import com.intersection.application.services.abstractions.UserService;
import com.intersection.domain.entity.User;
import com.intersection.infrastructure.security.JwtUtil;
import com.intersection.infrastructure.security.PasswordHashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordHashingService passwordHashingService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordHashingService passwordHashingService,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UUID registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = passwordHashingService.hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    public String authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordHashingService.verifyPassword(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtil.generateToken(user.getUsername());
    }
}