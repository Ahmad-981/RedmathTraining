package com.practice.project06.account;//package com.app.banking.account;


//import com.app.banking.customer.Customer;
import com.practice.project06.balance.Balance;
import com.practice.project06.transaction.Transaction;
import com.practice.project06.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountID")
    private Long accountID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "balanceID")
    private Balance balance;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    @Column(name = "password", nullable = false)
    private String password;

    // Getters and Setters
}
