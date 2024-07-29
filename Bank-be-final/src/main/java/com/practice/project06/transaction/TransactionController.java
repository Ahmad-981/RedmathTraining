package com.practice.project06.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/by-account")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@RequestParam Long id) {
        List<Transaction> transactions = transactionService.findTransactionsByAccountId(id);
        return ResponseEntity.ok(transactions);
    }

//    @PostMapping
//    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) {
//        Transaction transaction = transactionService.createTransaction(
//                transactionDTO.getFromAccountID(),
//                transactionDTO.getToAccountNumber(),
//                transactionDTO.getAmount()
//        );
//        return ResponseEntity.ok(transaction);
//    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            Transaction transaction = transactionService.createTransaction(
                    transactionDTO.getFromAccountID(),
                    transactionDTO.getToAccountNumber(),
                    transactionDTO.getAmount());
            return ResponseEntity.ok(transaction);
        } catch (IllegalArgumentException e) {
            // Return error response with message
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

}