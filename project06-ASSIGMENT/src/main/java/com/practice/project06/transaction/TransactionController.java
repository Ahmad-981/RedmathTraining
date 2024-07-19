package com.practice.project06.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import com.practice.project06.transaction.TransactionService;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


//    @GetMapping("/by-account")
//    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@RequestParam Long accountId) {
//        List<Transaction> transactions = transactionService.findTransactionsByAccountId(accountId);
//        return ResponseEntity.ok(transactions);
//    }
//
//    @PostMapping
//    public ResponseEntity<String> createTransaction(@RequestParam Long fromAccount,
//                                                    @RequestParam String toAccount,
//                                                    @RequestParam BigDecimal amount) {
//        transactionService.transfer(fromAccount, toAccount, amount);
//        return ResponseEntity.ok("Transaction successful");
//    }



    @GetMapping("/by-account")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@RequestParam Long accountId) {
        List<Transaction> transactions = transactionService.findTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestParam Long fromAccount,
                                                    @RequestParam String toAccount,
                                                    @RequestParam BigDecimal amount) {
        try {
            transactionService.transfer(fromAccount, toAccount, amount);
            return ResponseEntity.ok("Transaction successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}