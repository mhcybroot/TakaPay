package com.example.takapay.service.impl;

import com.example.takapay.entity.Transection;
import com.example.takapay.entity.User;
import com.example.takapay.repository.TransectionRepository;
import com.example.takapay.repository.UserRepository;
import com.example.takapay.service.TransectionService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransectionServiceImpl implements TransectionService {

    final private UserRepository userRepository;
    final private TransectionRepository transectionRepository;

    public TransectionServiceImpl(UserRepository userRepository, TransectionRepository transectionRepository) {
        this.userRepository = userRepository;
        this.transectionRepository = transectionRepository;
    }


    @Override
    @Transactional
    public void transferMoney(String username, Long receiverId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be positive");
        }


        User sender = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);

        Transection transection = Transection.builder()
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .amount(amount)
                .build();

        transectionRepository.save(transection);

    }

    @Override
    public Page<Transection> transectionHistory(String username, Pageable pageable) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return transectionRepository.findBySenderIdOrReceiverId(user.getId(), user.getId(), pageable);


    }
}
