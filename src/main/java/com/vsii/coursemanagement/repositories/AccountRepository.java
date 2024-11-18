package com.vsii.coursemanagement.repositories;

import com.vsii.coursemanagement.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account , Long > {
    Boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);

    @Query("UPDATE Account a SET a.password = :password WHERE a.id = :accountId")
    int updatePassword(@Param("password") String password, @Param("accountId") Long accountId);
}
