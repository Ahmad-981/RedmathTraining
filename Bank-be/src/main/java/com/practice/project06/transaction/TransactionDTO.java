package com.practice.project06.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class TransactionDTO {
    private Long fromAccountID;
    private String toAccountNumber;
    private BigDecimal amount;
    private String indicator;

    // Getters and Setters
}