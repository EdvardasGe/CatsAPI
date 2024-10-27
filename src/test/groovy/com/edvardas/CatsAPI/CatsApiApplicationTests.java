package com.edvardas.CatsAPI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("prod")
@SpringBootTest
class CatsApiApplicationTests {

	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12")
			.withDatabaseName("catsdb")
			.withUsername("admin")
			.withPassword("admin");

	static {
		postgresContainer.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

	@Test
	void contextLoads() {
	}

}
