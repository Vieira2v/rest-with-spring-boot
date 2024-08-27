package com.example.rest_with_spring_boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rest_with_spring_boot.model.Book;

public interface BooksRepository extends JpaRepository<Book, Long>{}
