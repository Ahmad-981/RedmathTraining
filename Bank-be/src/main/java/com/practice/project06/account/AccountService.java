package com.practice.project06.account;

import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.TransactionRepository;
import com.practice.project06.user.User;
import com.practice.project06.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

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

    public boolean deleteAccount(Long id) {
        Optional<Account> accountToDelete = accountRepository.findById(id);
        if (accountToDelete.isPresent()) {
            if(accountToDelete.get().getUser().getUsername().equals("admin") ){
                return false;
            }
            else {
                balanceRepository.deleteById(id);
                transactionRepository.deleteById(id);
                accountRepository.deleteById(id);

                return true;
            }
        }
        return false;
    }

}