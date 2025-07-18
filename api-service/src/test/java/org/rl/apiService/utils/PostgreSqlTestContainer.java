package org.rl.apiService.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface PostgreSqlTestContainer {
    @Container
    PostgreSQLContainer<?> PG_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void setPgSqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PG_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PG_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PG_SQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

}
