package com.example.integrationtests.vo.pagedmodels;

import java.util.List;

import com.example.integrationtests.vo.PersonVO;

public class PagedModelPerson {
    
    private List<PersonVO> content;

    public PagedModelPerson() {}

    public List<PersonVO> getContent() {
        return content;
    }

    public void setContent(List<PersonVO> content) {
        this.content = content;
    }

    
}
