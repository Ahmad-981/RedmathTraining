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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

//    //@Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private BalanceRepository balanceRepository;

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
    }

    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        // Fetch transactions where the account is the sender
        List<Transaction> sentTransactions = transactionRepository.findByFromAccount_AccountID(accountId);

        // Fetch transactions where the account is the receiver
        List<Transaction> receivedTransactions = transactionRepository.findByToAccount_AccountID(accountId);

        // Combine the lists and return
        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(sentTransactions);
        allTransactions.addAll(receivedTransactions);

        return allTransactions;
    }

//    @Transactional
//    public Transaction createTransaction(Long fromAccountId, String toAccountNumber, BigDecimal amount) {
//        // Retrieve the accounts
//        Account fromAccount = accountRepository.findById(fromAccountId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid from account ID"));
//
//        Optional<Account> toAccountOpt = accountRepository.findByAccountNumber(toAccountNumber);
//        if (toAccountOpt.isEmpty()) {
//            throw new IllegalArgumentException("Invalid to account number");
//        }
//        Account toAccount = toAccountOpt.get();
//
//        // Retrieve and validate balances
//        Balance fromBalance = balanceRepository.findBalanceByAccountId(fromAccount.getAccountID());
//        if (fromBalance == null) {
//            throw new IllegalArgumentException("No balance found for from account");
//        }
//
//        if (fromBalance.getAmount().compareTo(amount) < 0) {
//            throw new IllegalArgumentException("Low balance");
//        }
//
//        Balance toBalance = balanceRepository.findBalanceByAccountId(toAccount.getAccountID());
//        if (toBalance == null) {
//            toBalance = new Balance();
//            toBalance.setAccount(toAccount);
//            toBalance.setAmount(BigDecimal.ZERO);
//        }
//
//        // Debit from fromAccount
//        fromBalance.setAmount(fromBalance.getAmount().subtract(amount));
//        balanceRepository.save(fromBalance);
//
//        // Credit to toAccount
//        toBalance.setAmount(toBalance.getAmount().add(amount));
//        balanceRepository.save(toBalance);
//
//        // Create and save debit transaction
//        Transaction debitTransaction = new Transaction();
//        debitTransaction.setFromAccount(fromAccount);
//        debitTransaction.setToAccount(toAccount);
//        debitTransaction.setDate(new Date());
//        debitTransaction.setAmount(amount);
//        debitTransaction.setIndicator("DB");
//        transactionRepository.save(debitTransaction);
//
//        // Create and save credit transaction
//        Transaction creditTransaction = new Transaction();
//        creditTransaction.setFromAccount(fromAccount);
//        creditTransaction.setToAccount(toAccount);
//        creditTransaction.setDate(new Date());
//        creditTransaction.setAmount(amount);
//        creditTransaction.setIndicator("CR");
//        transactionRepository.save(creditTransaction);
//
//        // Optionally, return one of the transactions or both, depending on your use case
//        return debitTransaction; // or return creditTransaction; or return both
//    }


    @Transactional
    public Transaction createTransaction(Long fromAccountId, String toAccountNumber, BigDecimal amount) {
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
