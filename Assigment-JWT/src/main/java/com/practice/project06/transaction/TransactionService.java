package com.practice.project06.transaction;

import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.Balance;
import com.practice.project06.balance.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    //@Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByFromAccount_AccountID(accountId);
    }


    @Transactional
    public Transaction transfer(Long fromAccountId, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid from account ID"));

        Optional<Account> toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        if (toAccount == null) {
            throw new IllegalArgumentException("Invalid to account number");
        }

        Balance fromBalance = balanceRepository.findBalanceByAccountId(fromAccount.getAccountID());
        if (fromBalance.getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Low balance");
        }

        fromBalance.setAmount(fromBalance.getAmount().subtract(amount));
        balanceRepository.save(fromBalance);

        Balance toBalance = balanceRepository.findBalanceByAccountId(toAccount.get().getAccountID());
        toBalance.setAmount(toBalance.getAmount().add(amount));
        balanceRepository.save(toBalance);

        Transaction transaction = new Transaction(fromAccount, toAccount.get(), amount);
        return transactionRepository.save(transaction);
    }

}
