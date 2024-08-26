package com.example.rest_with_spring_boot.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.rest_with_spring_boot.data_vo_v2.PersonVoV2;
import com.example.rest_with_spring_boot.model.Person;

@Service
public class PersonMapper {
    
    public PersonVoV2 convertEntityToVo(Person person) {
        PersonVoV2 vo = new PersonVoV2();
        vo.setId(person.getId());
        vo.setFirstName(person.getFirstName());
        vo.setLastName(person.getLastName());
        vo.setAddress(person.getAddress());
        vo.setGender(person.getGender());
        vo.setBirthDay(new Date());
        return vo;
    }

    public Person convertVoToEntity(PersonVoV2 vo) {
        Person entity = new Person();
        entity.setId(vo.getId());
        entity.setFirstName(vo.getFirstName());
        entity.setLastName(vo.getLastName());
        entity.setAddress(vo.getAddress());
        entity.setGender(vo.getGender());
        return entity;
    }
}
