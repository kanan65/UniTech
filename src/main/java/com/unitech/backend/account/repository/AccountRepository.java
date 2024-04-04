package com.unitech.backend.account.repository;

import com.unitech.backend.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByIdentifier(String identifier);

    @Query(nativeQuery = true, value ="select a.id from accounts a " +
            "order by id desc limit 1")
    Optional<Long> lastAccountId();

    @Query(nativeQuery = true, value ="select * from accounts ac " +
            "where ac.user_id = :userId and ac.enabled = true ")
    List<Account> findUserAccounts(@Param("userId") long userId);

    @Query(nativeQuery = true, value ="select * from accounts ac " +
            "where ac.id = :id and ac.user_id = :userId ")
    Optional<Account> findAccountByIdAndUser(@Param("id") long id, @Param("userId") long userId);

    @Query(nativeQuery = true, value ="select * from accounts ac " +
            "where ac.identifier = UPPER(:identifier) and ac.user_id = :userId ")
    Optional<Account> findAccountByIdentifierAndUser(@Param("identifier") String identifier, @Param("userId") long userId);

}
