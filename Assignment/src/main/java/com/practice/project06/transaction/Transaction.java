package com.practice.project06.transaction;

//import com.app.banking.account.Account;
import com.practice.project06.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "transactions")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionID")
    private Long transactionID;

    @ManyToOne
    @JoinColumn(name = "accountID", nullable = false)
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "indicator", nullable = false)
    private String indicator;

    // Getters and Setters
}
