package com.practice.project06.account;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id).get();
    }

//    @PostMapping
//    public Account createAccount(@RequestBody Account account) {
//        return accountService.createAccount(account);
//    }
    @PostMapping
    public Long createAccount(@RequestBody AccountDTO accountDTO) {
        System.out.println("Received AccountDTO: " + accountDTO);
        return accountService.createAccount(accountDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody Account updatedAccount) {
        Optional<Account> updated = accountService.updateAccount(id, updatedAccount);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        boolean deleted = accountService.deleteAccount(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}