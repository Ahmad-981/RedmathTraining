package com.practice.project06.balance;

import com.practice.project06.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Query("SELECT b FROM balances b WHERE b.account.accountID = :accountID")
    Balance findBalanceByAccountId(@Param("accountID") Long accountID);
}
