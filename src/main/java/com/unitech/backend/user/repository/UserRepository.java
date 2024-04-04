package com.unitech.backend.user.repository;

import com.unitech.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value ="select * from user u " +
            "where u.username = :username and u.is_enabled = true")
    Optional<User> findUserByUsername(@Param("username") String username);
}
