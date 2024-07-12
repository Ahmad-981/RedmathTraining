package com.practice.project06.transaction;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServices {

    private final TransactionRepository transactionRepository;

    public TransactionServices(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    Optional<Transaction> findById(Long id){
        return transactionRepository.findById(id);
    }

    public List<Transaction> findAll(
            Integer page, Integer count
    ){
        if (page < 0) {
            page = 0;
        }
        if (count > 500) {
            count = 500;
        }
        return transactionRepository.findAll(PageRequest.of(page, count)).getContent();
    }

    public Transaction create(Transaction transaction){
//        student.setId(System.currentTimeMillis());
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        student.setName(username);
//        student.setCgpa("NA");
  //      student.setUniversity("Lums University");
        return transactionRepository.save(transaction);

    }

//    public Optional<Transaction> update(Long id, Transaction transaction) {
//        Optional<Transaction> existing = transactionRepository.findById(id);
//        if (existing.isPresent()) {
//            existing.get().setAmount(transaction.getAmount());
//            existing.get().setRoll_num(student.getRoll_num());
//            existing.get().setCgpa(student.getCgpa());
//            existing.get().setUniversity(student.getUniversity());
//            existing = Optional.of(studentRepository.save(existing.get()));
//        }
//        return existing;
//    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

}
