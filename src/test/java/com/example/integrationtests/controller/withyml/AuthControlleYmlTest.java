package com.example.integrationtests.controller.withyml;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.example.rest_with_spring_boot.Startup;
import com.example.rest_with_spring_boot.data_vo_v1.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import static io.restassured.RestAssured.given;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@ActiveProfiles("test")
@SpringBootTest(classes = Startup.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControlleYmlTest extends AbstractIntegrationTest {

	private static YMLmapper mapper;
    private static TokenVO tokenVO;

	@BeforeAll
	public static void setUp() {
		mapper = new YMLmapper();
	}
    
    @Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredencialVO user = 
				new AccountCredencialVO("leandro", "admin123");
		
		tokenVO = given()
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.body(user, mapper)
                .when()
				.post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, mapper);
		
		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());
	}

	@Test
	@Order(2)
	public void testRefreshToken() throws JsonMappingException, JsonProcessingException {
				
		var newTokenVO = given()
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("username", tokenVO.getUsername())
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
				.put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, mapper);
		
		assertNotNull(newTokenVO.getAccessToken());
		assertNotNull(newTokenVO.getRefreshToken()); 
	}
}
