package com.example.rest_with_spring_boot.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.rest_with_spring_boot.data_vo_v1.BookVO;
import com.example.rest_with_spring_boot.model.Book;

public class MockBook {
    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setTitle("Title Test" + number);
        book.setAuthor("Author Test" + number);
        book.setPrice(200D);
        book.setId(number.longValue()); 
        book.setLaunchDate(new Date());
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setTitle("Title Test" + number);
        book.setAuthor("Author Test" + number);
        book.setPrice(200D);
        book.setKey(number.longValue());
        book.setLaunchDate(new Date());
        return book;
    }
}
