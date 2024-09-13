package com.example.integrationtests.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

import com.example.integrationtests.testscontainers.AbstractIntegrationTest;
import com.example.rest_with_spring_boot.Startup;
import com.example.rest_with_spring_boot.model.Person;
import com.example.rest_with_spring_boot.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@ActiveProfiles("test")
@SpringBootTest(classes = Startup.class)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{
    
    @Autowired
    public PersonRepository repository;

    private static Person person;

    @BeforeAll //Este cara vai ser executado antes de todos os testes.
    public static void setUp() {
        person = new Person();
    }

    @Test
	@Order(1)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));

        person = repository.findPersonsByName("be", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertTrue(person.isEnabled());
		
		assertEquals(69, person.getId());
		
		assertEquals("Abbe", person.getFirstName());
		assertEquals("Kytley", person.getLastName());
		assertEquals("69618 Aberg Court", person.getAddress());
		assertEquals("Male", person.getGender());
	}

    @Test
	@Order(2)
	public void testDisabledPerson() throws JsonMappingException, JsonProcessingException {
		
        repository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));

        person = repository.findPersonsByName("be", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertFalse(person.isEnabled());
		
		assertEquals(69, person.getId());
		
		assertEquals("Abbe", person.getFirstName());
		assertEquals("Kytley", person.getLastName());
		assertEquals("69618 Aberg Court", person.getAddress());
		assertEquals("Male", person.getGender());
	}

}
