package com.example.rest_with_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rest_with_spring_boot.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{}
