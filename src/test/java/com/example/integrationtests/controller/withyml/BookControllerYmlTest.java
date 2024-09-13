package com.example.integrationtests.controller.withyml;

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
import com.example.integrationtests.controller.withyml.mapper.YMLmapper;
import com.example.integrationtests.testscontainers.AbstractIntegrationTest;
import com.example.integrationtests.vo.AccountCredencialVO;
import com.example.integrationtests.vo.BookVO;
import com.example.integrationtests.vo.pagedmodels.PagedModelBook;
import com.example.rest_with_spring_boot.Startup;
import com.example.rest_with_spring_boot.data_vo_v1.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@SpringBootTest(classes = Startup.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYmlTest extends AbstractIntegrationTest{
	
	private static RequestSpecification specification;
	private static YMLmapper objectMapper;

	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YMLmapper();
		
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
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper) // Enviando o objeto user no corpo da requisição
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class, objectMapper)
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
		
		var persistedBook = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(book, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(BookVO.class, objectMapper);
		
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
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setAuthor("Roger Flores");
		
		var persistedBook = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(book, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getLaunchDate());
		
		assertEquals(book.getKey(), persistedBook.getKey());
		
		assertEquals("Pinóquio", persistedBook.getTitle());
		assertEquals("Roger Flores", persistedBook.getAuthor());
        assertEquals(125.50, persistedBook.getPrice(), 0.001);
        assertNotNull(persistedBook.getLaunchDate());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var persistedBook = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", book.getKey())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(BookVO.class, objectMapper);
		
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getTitle());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getLaunchDate());
		
		assertEquals(book.getKey(), persistedBook.getKey());
		
		assertEquals("Pinóquio", persistedBook.getTitle());
		assertEquals("Roger Flores", persistedBook.getAuthor());
        assertEquals(125.50, persistedBook.getPrice(), 0.001);
        assertNotNull(persistedBook.getLaunchDate());
	}

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", book.getKey())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204);
	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var wrapper = given().spec(specification)
			.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 1, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(PagedModelBook.class, objectMapper);

		var books = wrapper.getContent();
		
		BookVO foundBookOne = books.get(0);
		
		assertNotNull(foundBookOne.getKey());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getPrice());
		assertNotNull(foundBookOne.getLaunchDate());
		
		assertEquals(10, foundBookOne.getKey());
		
		assertEquals("O poder dos quietos", foundBookOne.getTitle());
		assertEquals("Susan Cain", foundBookOne.getAuthor());
        assertEquals(123, foundBookOne.getPrice(), 0.001);
        assertNotNull(foundBookOne.getLaunchDate());

		BookVO foundBookSix = books.get(2);
		
		assertNotNull(foundBookSix.getKey());
		assertNotNull(foundBookSix.getTitle());
		assertNotNull(foundBookSix.getAuthor());
		assertNotNull(foundBookSix.getPrice());
		assertNotNull(foundBookSix.getLaunchDate());
		
		assertEquals(13, foundBookSix.getKey());
		
		assertEquals("O verdadeiro valor de TI", foundBookSix.getTitle());
		assertEquals("Richard Hunter", foundBookSix.getAuthor());
        assertEquals(95, foundBookSix.getPrice(), 0.001);
        assertNotNull(foundBookSix.getLaunchDate());
	}

	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
		.setBasePath("/api/book/v1")
		.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		.build();

		given().spec(specificationWithoutToken)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.when()
					.get()
				.then()
					.statusCode(403);
	}

	@Test
	@Order(7)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var wrapper = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 0, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();



		assertTrue(wrapper.contains(" - rel: \"self\"\n" +
						"    href: \"http://localhost:8888/api/book/v1/4\""));
		assertTrue(wrapper.contains("- rel: \"self\"\n" +
						"    href: \"http://localhost:8888/api/book/v1/4\""));
		assertTrue(wrapper.contains(" - rel: \"self\"\n" +
						"    href: \"http://localhost:8888/api/book/v1/15\""));

		assertTrue(wrapper.contains("""
                                            links:
                                            - rel: "first"
                                              href: "http://localhost:8888/api/book/v1?direction=asc&page=0&size=10&sort=title,asc"""));
		assertTrue(wrapper.contains("""
                                            - rel: "self"
                                              href: "http://localhost:8888/api/book/v1?page=0&size=10&direction=asc"""));
		assertTrue(wrapper.contains("""
                                            - rel: "next"
                                              href: "http://localhost:8888/api/book/v1?direction=asc&page=1&size=10&sort=title,asc"""));
		assertTrue(wrapper.contains("""
                                            - rel: "last"
                                              href: "http://localhost:8888/api/book/v1?direction=asc&page=1&size=10&sort=title,asc"""));									  

		assertTrue(wrapper.contains("""
                                            page:
                                              size: 10
                                              totalElements: 15
                                              totalPages: 2
                                              number: 0"""));
	}
}
