package com.example.takapay.controller;

import com.example.takapay.dto.TransferRequest;
import com.example.takapay.entity.Transection;
import com.example.takapay.repository.UserRepository;
import com.example.takapay.service.TransectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Repository("/api/transection")
@Tag(name = "Transection Management", description = "Endpoints for sending money and viewing history")
public class TransectionController {

    final private TransectionService transectionService;
    final private UserRepository userRepository;

    public TransectionController(TransectionService transectionService, UserRepository userRepository) {
        this.transectionService = transectionService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Send Money",description = "Transfers funds from the logged-in user to the receiver.")
    @PostMapping("/send")
    public ResponseEntity sendMoney(@RequestBody TransferRequest request, Principal principal) {
        transectionService.transferMoney(
                principal.getName(),
                request.getReceiverId(),
                request.getAmount()
        );
        return ResponseEntity.ok(("Transfer Successful"));
    }

    @Operation(summary = "Get History",description = "Retrieve paginated transection history.")
    @GetMapping("history")
    public ResponseEntity<?> history(Principal principal, Pageable pageable) {
        Page<Transection> transections = transectionService.transectionHistory(principal.getName(), pageable);
        return ResponseEntity.ok(transections);
    }
}

