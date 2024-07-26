package com.practice.project06.balance;

import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private AccountRepository accountRepository;

//    @PostMapping
//    public ResponseEntity<Balance> createBalance(@RequestBody BalanceDTO balanceDTO) {
//        Long accountId = balanceDTO.getAccountID();
//        Account account = accountRepository.findById(accountId)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        Balance balance = new Balance();
//        balance.setAmount(balanceDTO.getAmount());
//        balance.setIndicator(balanceDTO.getIndicator());
//        balance.setAccount(account);
//        balance.setDate(new Date());
//
//        Balance savedBalance = balanceService.createBalance(balance);
//        return ResponseEntity.ok(savedBalance);
//    }

    @PostMapping
    public ResponseEntity<Balance> createBalance(@RequestBody BalanceDTO balanceDTO) {
        Long accountId = balanceDTO.getAccountID();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Check if a balance already exists for the account
        Optional<Balance> existingBalance = balanceRepository.findByAccount(account);
        if (existingBalance.isPresent()) {
            throw new RuntimeException("Balance already exists for this account");
        }

        Balance balance = new Balance();
        balance.setAmount(balanceDTO.getAmount());
        balance.setIndicator(balanceDTO.getIndicator());
        balance.setAccount(account);
        balance.setDate(new Date());

        Balance savedBalance = balanceService.createBalance(balance);
        return ResponseEntity.ok(savedBalance);
    }

//    @GetMapping
//    public List<Balance> getAllAccountsBalance() {
//        return balanceRepository.findAll();
//    }

    @GetMapping("/{id}")
    public Balance getBalanceById(@PathVariable Long id) {
        return balanceRepository.findBalanceByAccountId(id);
    }

//    @PostMapping
//    public Balance createBalance(@RequestBody Balance balance) {
//        balance.setDate(new Date());
//        return balanceRepository.save(balance);
//    }

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