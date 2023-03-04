package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.dto.RestApiError;
import ru.kubsu.geocoder.util.TestUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestControllerTest {

  @LocalServerPort
  Integer port;

  @BeforeAll
  static void beforeAll() {
    System.out.println("BeforeAll");
  }

  @BeforeEach
  void setUp() {
    System.out.println("BeforeEach");
  }

  @AfterEach
  void tearDown() {
    System.out.println("AfterEach");

  }

  @Test
  void getTest() {
    assertEquals(3, TestUtil.sum(1, 2));
  }

  @Test
  void integrationTest() {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    ResponseEntity<ru.kubsu.geocoder.model.Test> response = testRestTemplate.
            getForEntity("http://localhost:" + port + "/tests/1?name=test", ru.kubsu.geocoder.model.Test.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    final ru.kubsu.geocoder.model.Test body = response.getBody();
    assertEquals(1, body.getId());
    assertEquals("test", body.getName());
    assertEquals(false, body.getDone());
    assertEquals(null, body.getMark());
  }
  @Test
  void integrationTestWhereIsNull() {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    ResponseEntity<Map<String, String>> response = testRestTemplate.
      exchange("http://localhost:" + port + "/tests/1",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<Map<String, String>>() {});

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    final Map<String, String> body = response.getBody();
    assertEquals("400", body.get("status"));
    assertEquals("Bad Request", body.get("error"));
    assertEquals("/tests/1", body.get("path"));
  }

  @Test
  void integrationTestWhereIsString() {
    TestRestTemplate testRestTemplate = new TestRestTemplate();
    ResponseEntity<RestApiError> response = testRestTemplate.
      exchange("http://localhost:" + port + "/tests/abc?name=test",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestApiError>() {});

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    final RestApiError body = response.getBody();
    assertEquals(400, body.getStatus());
    assertEquals("Bad Request", body.getError());
    assertEquals("/tests/abc", body.getPath());
  }

  @AfterAll
  static void afterAll() {
    System.out.println("AfterAll");
  }
}
