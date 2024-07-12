package com.practice.project06.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id).get();
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

//    @PutMapping("/{id}")
//    public Account updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
//        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
//        account.setPassword(accountDetails.getPassword());
//        account.setBalance(accountDetails.getBalance());
//        return accountRepository.save(account);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
//        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account
//    }
}