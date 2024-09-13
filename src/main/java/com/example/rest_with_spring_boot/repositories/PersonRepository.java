package com.example.rest_with_spring_boot.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rest_with_spring_boot.model.Person;

import jakarta.transaction.Transactional;

public interface PersonRepository extends JpaRepository<Person, Long>{

    @Transactional
    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") long id);

    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%', :firstName, '%'))")
    Page<Person> findPersonsByName(@Param("firstName") String firstName, Pageable pageable);
}
