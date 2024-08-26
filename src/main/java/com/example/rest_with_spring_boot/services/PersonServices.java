package com.example.rest_with_spring_boot.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.example.rest_with_spring_boot.controllers.PersonController;
import com.example.rest_with_spring_boot.data_vo_v1.PersonVO;
import com.example.rest_with_spring_boot.data_vo_v2.PersonVoV2;
import com.example.rest_with_spring_boot.exceptions.RequiredObjectsIsNullException;
import com.example.rest_with_spring_boot.exceptions.ResourceNotFoundException;
import com.example.rest_with_spring_boot.mapper.DozerMapper;
import com.example.rest_with_spring_boot.mapper.custom.PersonMapper;
import com.example.rest_with_spring_boot.model.Person;
import com.example.rest_with_spring_boot.repositories.PersonRepository;

@Service
public class PersonServices {
    private static final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    public List<PersonVO> findAll() {
        logger.info("Finding all people!");

        var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
        
        persons.stream().forEach(
            p -> p.add(linkTo(
                methodOn(PersonController.class)
                .findById(p.getKey())).withSelfRel()));

        return persons;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {

        if (person == null) throw new RequiredObjectsIsNullException();
        logger.info("Creating one person!");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVoV2 createV2(PersonVoV2 person) {

        if (person == null) throw new RequiredObjectsIsNullException();
        logger.info("Creating one person with V2!");
        var entity = mapper.convertVoToEntity(person);
        var vo = mapper.convertEntityToVo(repository.save(entity));
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {

        if (person == null) throw new RequiredObjectsIsNullException();
        logger.info("Updating one person!");

        var entity = repository.findById(person.getKey())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setGender(person.getGender());
        entity.setAddress(person.getAddress());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }
}
