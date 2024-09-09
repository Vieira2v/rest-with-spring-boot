package com.example.rest_with_spring_boot.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.example.rest_with_spring_boot.controllers.BookController;
import com.example.rest_with_spring_boot.data_vo_v1.BookVO;
import com.example.rest_with_spring_boot.exceptions.RequiredObjectsIsNullException;
import com.example.rest_with_spring_boot.exceptions.ResourceNotFoundException;
import com.example.rest_with_spring_boot.mapper.DozerMapper;
import com.example.rest_with_spring_boot.model.Book;
import com.example.rest_with_spring_boot.repositories.BooksRepository;


@Service
public class BookServices {
    private static final Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    private BooksRepository repository;

    public List<BookVO> findAll() {
        logger.info("Finding all books!");

        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);

        books.stream().forEach(
            b -> b.add(linkTo(
                methodOn(BookController.class)
                .findById(b.getKey())).withSelfRel())
        );

        return books;
    }

    public BookVO findById(Long id) {
        logger.info("Finding one book!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var books = DozerMapper.parseObject(entity, BookVO.class);

        books.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

        return books;
    }

    public BookVO create(BookVO book) {
        if(book == null) throw new RequiredObjectsIsNullException();
        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(book, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);

        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public BookVO update(BookVO book) {
        if(book == null) throw new RequiredObjectsIsNullException();
        logger.info("Updating one book!");

        var entity = repository.findById(book.getKey())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setPrice(book.getPrice());
        entity.setLaunchDate(book.getLaunchDate());
        
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);

        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }

}