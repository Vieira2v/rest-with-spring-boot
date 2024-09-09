package com.example.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.configs.TestConfigs;
import com.example.integrationtests.testscontainers.AbstractIntegrationTest;
import com.example.integrationtests.vo.AccountCredencialVO;
import com.example.rest_with_spring_boot.Startup;
import com.example.rest_with_spring_boot.data_vo_v1.security.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootTest(classes = Startup.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControlleJsonTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;
    
    @Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		
		AccountCredencialVO user = 
				new AccountCredencialVO("leandro", "admin123");
		
		tokenVO = given()
				.basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
                .when()
				.post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class);
		
		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());
	}
}
