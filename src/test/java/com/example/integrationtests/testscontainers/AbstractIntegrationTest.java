package com.example.integrationtests.testscontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers=AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:17beta3");

        private void startContainers() {
            Startables.deepStart(Stream.of(postgresql)).join();
        }

        private static Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                "spring.datasource.url", postgresql.getJdbcUrl(),
                "spring.datasource.username", postgresql.getUsername(),
                "spring.datasource.password", postgresql.getPassword(),
                "spring.flyway.url", postgresql.getJdbcUrl(),
                "spring.flyway.user", postgresql.getUsername(),
                "spring.flyway.password", postgresql.getPassword()
            );
        }
        

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            String jdbcUrl = postgresql.getJdbcUrl();
            String username = postgresql.getUsername();
            String password = postgresql.getPassword();
            
            // Imprima a URL do banco de dados
            System.out.println("JDBC URL: " + jdbcUrl);
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
            
            MapPropertySource testcontainers = new MapPropertySource(
                "testcontainers", 
                (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(testcontainers);

            Flyway flyway = Flyway.configure()
                .dataSource(postgresql.getJdbcUrl(), postgresql.getUsername(), postgresql.getPassword())
                .locations("classpath:/db/migration") // Certifique-se de que a localização está correta
                .load();
            flyway.migrate();
        }
    }
}
