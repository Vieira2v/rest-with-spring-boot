package com.example.integrationtests.controller.cors;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.configs.TestConfigs;
import com.example.integrationtests.testscontainers.AbstractIntegrationTest;
import com.example.integrationtests.vo.AccountCredencialVO;
import com.example.integrationtests.vo.BookVO;
import com.example.rest_with_spring_boot.Startup;
import com.example.rest_with_spring_boot.data_vo_v1.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@SpringBootTest(classes = Startup.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerCorsJsonTest extends AbstractIntegrationTest{
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		book = new BookVO();
	}
	
    private void mockBook() {
		book.setTitle("Pinóquio");
		book.setAuthor("Miguel Falabela");
		book.setPrice(125.50);
		book.setLaunchDate(new Date());
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredencialVO user = new AccountCredencialVO("leandro", "admin123");
		
		System.out.println(new ObjectMapper().writeValueAsString(user));
    
		var accessToken = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user) // Enviando o objeto user no corpo da requisição
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class)
				.getAccessToken();

		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(book)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getLaunchDate());
		
		assertTrue(persistedBook.getKey() > 0);
		
		assertEquals("Pinóquio", persistedBook.getTitle());
		assertEquals("Miguel Falabela", persistedBook.getAuthor());
        assertEquals(125.50, persistedBook.getPrice(), 0.001);
        assertNotNull(persistedBook.getLaunchDate());
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockBook();
	
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
					.body(book)
				.when()
					.post()
				.then()
					.statusCode(403)
						.extract()
							.body()
								.asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", book.getKey())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getKey());
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getLaunchDate());
		
		assertTrue(persistedBook.getKey() > 0);
		
		assertEquals("Pinóquio", persistedBook.getTitle());
		assertEquals("Miguel Falabela", persistedBook.getAuthor());
        assertEquals(125.50, persistedBook.getPrice(), 0.001);
        assertNotNull(persistedBook.getLaunchDate());
	}
	
	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
					.pathParam("id", book.getKey())
					.when()
					.get("{id}")
				.then()
					.statusCode(403)
						.extract()
						.body()
							.asString();

		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
}
