package com.practice.project06.account;

import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.TransactionRepository;
import com.practice.project06.user.User;
import com.practice.project06.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;



    private String generateRandomString(int length) {
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        int randomInteger = random.nextInt((max - min) + 1) + min;
        return String.valueOf(randomInteger);
    }

    public Long createAccount(AccountDTO accountDTO) {
        User user = accountDTO.getUser();

        if (user == null || user.getUserID() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User existingUser = userRepository.findById(user.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setUser(existingUser);
        account.setAccountNumber(generateRandomString(8));

        Account savedAccount = accountRepository.save(account);
        return savedAccount.getAccountID();
    }

    public Optional<Account> updateAccount(Long id, Account updatedAccount) {
        Optional<Account> existingAccountOpt = accountRepository.findById(id);

        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();

            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setAccountType(updatedAccount.getAccountType());

            User updatedUser = updatedAccount.getUser();
            if (updatedUser != null) {
                User existingUser = existingAccount.getUser();
                if (existingUser != null) {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setRole(updatedUser.getRole());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setAddress(updatedUser.getAddress());
                    userRepository.save(existingUser);
                }
            }
            return Optional.of(accountRepository.save(existingAccount));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public boolean deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        balanceRepository.deleteByAccountId(id);
        transactionRepository.deleteByFromAccountId(id);
        accountRepository.delete(account);
        return true;

    }

}