package com.practice.project06.account;

import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account createAccount(Account account) {
        // Encode the password before saving
        //account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccount(Long id, Account updatedAccount) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            updatedAccount.setAccountID(id); // Ensure ID is set for update
            return Optional.of(accountRepository.save(updatedAccount));
        } else {
            return Optional.empty();
        }
    }


    public boolean deleteAccount(Long id) {
        Optional<Account> accountToDelete = accountRepository.findById(id);
        if (accountToDelete.isPresent()) {

            balanceRepository.deleteById(id);
            transactionRepository.deleteById(id);
            accountRepository.deleteById(id);

            return true;
        }
        return false;
    }

}