package com.example.takapay.service;

import com.example.takapay.dto.RegisterDto;
import com.example.takapay.entity.User;

import java.math.BigDecimal;

public interface UserService {
    User register(RegisterDto request);

    User login(String username, String Password);

    BigDecimal checkBalance(String username);
}
