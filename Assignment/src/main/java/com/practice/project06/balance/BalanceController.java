package com.practice.project06.balance;

import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    @Autowired
    private BalanceRepository balanceRepository;

    @GetMapping
    public List<Balance> getAllAccounts() {
        return balanceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Balance getAccountById(@PathVariable Long id) {
        return balanceRepository.findById(id).get();
    }

    @PostMapping
    public Balance createAccount(@RequestBody Balance balance) {
        return balanceRepository.save(balance);
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