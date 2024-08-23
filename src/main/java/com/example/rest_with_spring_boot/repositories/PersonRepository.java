package com.example.rest_with_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rest_with_spring_boot.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{}
