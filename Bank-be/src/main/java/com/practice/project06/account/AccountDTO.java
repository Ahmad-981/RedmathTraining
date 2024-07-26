package com.practice.project06.account;


import com.practice.project06.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
    private User user;
    private String accountNumber;
    private String accountType;

    // Getters and Setters
}