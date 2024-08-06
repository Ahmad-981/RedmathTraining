package com.practice.project06.transaction;

import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.Balance;
import com.practice.project06.balance.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        // Fetch transactions where the accountId is the sender
        List<Transaction> sentTransactions = transactionRepository.findByFromAccount_AccountID(accountId);

        // Fetch transactions where the accountId is the receiver
        List<Transaction> receivedTransactions = transactionRepository.findByToAccount_AccountID(accountId);

        // Combine all transactions into a Set to ensure uniqueness
        Set<Transaction> uniqueTransactions = new HashSet<>();
        uniqueTransactions.addAll(sentTransactions);
        uniqueTransactions.addAll(receivedTransactions);

        // Filter out transactions where the 'fromAccount' or 'toAccount' is soft-deleted
        List<Transaction> validTransactions = uniqueTransactions.stream()
                .filter(transaction -> {
                    boolean fromAccountValid = transaction.getFromAccount() != null && !transaction.getFromAccount().getIsDeleted();
                    boolean toAccountValid = transaction.getToAccount() != null && !transaction.getToAccount().getIsDeleted();
                    return fromAccountValid || toAccountValid;
                })
                .collect(Collectors.toList());

        return validTransactions;
    }

//    public List<Transaction> findTransactionsByAccountId(Long accountId) {
//        List<Transaction> sentTransactions = transactionRepository.findByFromAccount_AccountID(accountId);
//
//        List<Transaction> receivedTransactions = transactionRepository.findByToAccount_AccountID(accountId);
//
//        List<Transaction> allTransactions = new ArrayList<>();
//        allTransactions.addAll(sentTransactions);
//        allTransactions.addAll(receivedTransactions);
//
//        return allTransactions;
//    }

    @Transactional
    public Transaction createTransaction(Long fromAccountId, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid from account ID"));

        Optional<Account> toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        if (toAccount.isEmpty()) {
            throw new IllegalArgumentException("Invalid receiver account number");
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
