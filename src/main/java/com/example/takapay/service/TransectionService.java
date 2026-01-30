package com.example.takapay.service;


import com.example.takapay.entity.Transection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface TransectionService {

    void transferMoney(String  username, Long receiverId, BigDecimal amount);

    Page<Transection> transectionHistory(String username, Pageable pageable);
}
