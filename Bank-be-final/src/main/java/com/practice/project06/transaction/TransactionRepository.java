package com.practice.project06.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Finds transactions where the 'fromAccount' ID matches
    List<Transaction> findByFromAccount_AccountID(Long accountId);

    // Finds transactions where the 'toAccount' ID matches
    List<Transaction> findByToAccount_AccountID(Long accountId);

    // Deletes transactions where the 'fromAccount' ID matches
    @Modifying
    @Query("DELETE FROM transactions t WHERE t.fromAccount.accountID = :accountId")
    void deleteByFromAccountId(@Param("accountId") Long accountId);

    // Deletes transactions where the 'toAccount' ID matches
    @Modifying
    @Query("DELETE FROM transactions t WHERE t.toAccount.accountID = :accountId")
    void deleteByToAccountId(@Param("accountId") Long accountId);

    // Finds transactions where the 'fromAccount' ID matches and 'toAccount' is not deleted
    @Query("SELECT t FROM transactions t WHERE t.fromAccount.accountID = :fromAccountId AND t.toAccount.isDeleted = false")
    List<Transaction> findByFromAccount_AccountIDAndToAccount_IsDeletedFalse(@Param("fromAccountId") Long fromAccountId);

    // Finds transactions where the 'toAccount' ID matches and 'toAccount' is not deleted
    @Query("SELECT t FROM transactions t WHERE t.toAccount.accountID = :toAccountId AND t.toAccount.isDeleted = false")
    List<Transaction> findByToAccount_AccountIDAndToAccount_IsDeletedFalse(@Param("toAccountId") Long toAccountId);
}
