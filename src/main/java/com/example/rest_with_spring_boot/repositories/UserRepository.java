package com.example.rest_with_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.rest_with_spring_boot.model.User;

    @Repository
    public interface UserRepository extends JpaRepository<User, Long>{

        @Query("SELECT u FROM User u WHERE u.username =:username")
        User findByUsername(@Param("username") String username);
    }
