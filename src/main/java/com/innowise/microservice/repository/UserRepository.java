package com.innowise.microservice.repository;

import com.innowise.microservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {



    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);


    @Modifying
    @Query("update User u set u.active = :active where u.id = :id")
    void updateUserStatus(Long id,boolean active);



}
