package com.example.integrationtests.vo.pagedmodels;

import java.util.List;

import com.example.integrationtests.vo.BookVO;

public class PagedModelBook {
    
    private List<BookVO> content;

    public PagedModelBook() {}

    public List<BookVO> getContent() {
        return content;
    }

    public void setContent(List<BookVO> content) {
        this.content = content;
    }

    
}
