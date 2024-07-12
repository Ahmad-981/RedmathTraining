package com.practice.project06.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class TransactionController {

    private final TransactionServices transactionServices;

    public TransactionController(TransactionServices transactionServices) {
        this.transactionServices = transactionServices;
    }

    //GET Request
    @GetMapping("/api/v1/transaction/{id}")
    public ResponseEntity<Transaction> get(@PathVariable("id") Long id) {
        Optional<Transaction> transaction = transactionServices.findById(id);
        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction.get());
    }

    @GetMapping("/api/v1/transaction")
    public ResponseEntity<List<Transaction>> get(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "page", defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(transactionServices.findAll(page, size));
    }

    //POST Request
    //@PreAuthorize("hasAnyAuthority('student', 'admin')")
//    @PostMapping("/api/v1/student")
//    public ResponseEntity<Student> create(@RequestBody Transaction transaction){
//        transaction = transactionServices.create(transaction);
//        return ResponseEntity.created(URI.create("/api/v1/student" + transaction.getId()) ).body(transaction);
//    }


//    //PUT Request
//    //@PreAuthorize("hasAnyAuthority('teacher', 'admin')")
//    @PutMapping("/api/v1/student/{id}")
//    public ResponseEntity<Student> update(@PathVariable("id") Long id, @RequestBody Student student) {
//        Optional<Student> saved = studentServices.update(id, student);
//        if (saved.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(saved.get());
//    }
//
//    //Delete Request
//    //@PreAuthorize("hasAnyAuthority('admin')")
//    @DeleteMapping("/api/v1/student/{id}")
//    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
//        Optional<Student> student = studentServices.findById(id);
//        if (student.isPresent()) {
//            studentServices.deleteById(id);
//            return ResponseEntity.ok("Student record deleted successfully");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
