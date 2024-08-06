package com.practice.project06.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUser_UserID(Long userId);
    Optional<Account> findByUser_Username(String username);
    @Query("SELECT a FROM accounts a WHERE a.accountID = :accountID AND a.isDeleted = false")
    Optional<Account> findByAccountIDAndIsDeletedFalse(@Param("accountID") Long accountID);

}
