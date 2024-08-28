package com.example.integrationtests.swagger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.configs.TestConfigs;
import com.example.integrationtests.testscontainers.AbstractIntegrationTest;
import com.example.rest_with_spring_boot.Startup;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = Startup.class, webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest{

	@Test
	public void showDisplaySwaggerUiPage() {
		
		System.out.println("Starting test for Swagger UI");

		var content = 
			given()
				.basePath("/swagger-ui/index.html")
				.port(TestConfigs.SERVER_PORT)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body().asString();

		assertTrue(content.contains("Swagger UI"));

		System.out.println("Swagger UI page content verified successfully");
	}
}
