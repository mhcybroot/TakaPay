package com.example.takapay.service.impl;

import com.example.takapay.dto.RegisterDto;
import com.example.takapay.entity.User;
import com.example.takapay.repository.UserRepository;
import com.example.takapay.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    final private UserRepository repository;
    final private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public User register(RegisterDto request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(BigDecimal.ZERO)
                .username(request.getUsername())
                .build();

        return repository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = repository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }
        return user;
    }

    @Override
    public BigDecimal checkBalance(String username) {
        return repository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found")).getBalance();
    }
}
