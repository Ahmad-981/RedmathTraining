package com.practice.project06.balance;
import com.practice.project06.account.Account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity(name = "balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balanceID")
    private Long balanceID;

    //(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "accountID", nullable = false, unique = true)
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "indicator", nullable = false)
    private String indicator;

}
