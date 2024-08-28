package com.example.integrationtests.swagger;

import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class SimpleTestContainer {
    
    @Test
    public void testPostgresContainer() {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17beta3")) {
            postgres.start();
            System.out.println("PostgreSQL Container URL: " + postgres.getJdbcUrl());
            System.out.println("PostgreSQL Container Username: " + postgres.getUsername());
            System.out.println("PostgreSQL Container Password: " + postgres.getPassword());
        }
    }
}
