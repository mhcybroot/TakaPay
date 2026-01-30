package com.example.takapay.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
    @NotNull(message = "Amount is required")
    @Min(value = 1,message = "Amount must be greater then 0")
    private BigDecimal amount;
}
