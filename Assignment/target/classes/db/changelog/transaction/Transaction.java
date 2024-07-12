package com.app.banking.transaction;

//import com.app.banking.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    @Column(nullable = false)
    private Double amount;

    //@Column(nullable = false)
    private LocalDateTime timestamp;

    //@ManyToOne
    //@JoinColumn(name = "accountId", referencedColumnName = "accountID")
    //private Account account;

    //@Column(nullable = false)
    private Long toAccount;
}
