package com.example;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreContainerTest {

    @Test
    public void testPostgreSQLContainer() {
        PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>("postgres:17beta3");
        postgresql.start();
        
        // Verifique as informações do banco de dados
        System.out.println("JDBC URL: " + postgresql.getJdbcUrl());
        System.out.println("Username: " + postgresql.getUsername());
        System.out.println("Password: " + postgresql.getPassword());
    }
}
