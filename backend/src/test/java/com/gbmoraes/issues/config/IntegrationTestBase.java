package com.gbmoraes.issues.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestRestTemplate
public abstract class IntegrationTestBase {

  static final PostgreSQLContainer postgres;

  static {
    postgres = new PostgreSQLContainer("postgres:17-alpine")
      .withDatabaseName("todo_test")
      .withUsername("test")
      .withPassword("test");
    postgres.start();
  }

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.flyway.enabled", () -> "true");
    registry.add(
      "jwt.secret",
      () -> "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"
    );
  }

  @Autowired
  protected TestRestTemplate restTemplate;

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  @BeforeEach
  void cleanUp() {
    jdbcTemplate.execute("DELETE FROM issues");
    jdbcTemplate.execute("DELETE FROM users");
  }
}
