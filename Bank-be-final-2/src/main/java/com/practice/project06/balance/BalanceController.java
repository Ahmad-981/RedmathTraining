package com.practice.project06.balance;

import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    public ResponseEntity<?> createBalance(@RequestBody BalanceDTO balanceDTO) {
        Long accountId = balanceDTO.getAccountID();

        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account not found for ID: " + accountId);
        }
        Optional<Balance> existingBalance = balanceRepository.findByAccount(account);
        if (existingBalance.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Balance already exists for this account");
        }

        Balance balance = new Balance();
        balance.setAmount(balanceDTO.getAmount());
        balance.setIndicator(balanceDTO.getIndicator());
        balance.setAccount(account);
        balance.setDate(new Date());

        Balance savedBalance = balanceService.createBalance(balance);
        return ResponseEntity.ok(savedBalance);
    }

    @GetMapping("/{id}")
    public Balance getBalanceById(@PathVariable Long id) {
        return balanceRepository.findBalanceByAccountId(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public List<Balance> getAllBalances() {
        return balanceRepository.findAll();
    }
}