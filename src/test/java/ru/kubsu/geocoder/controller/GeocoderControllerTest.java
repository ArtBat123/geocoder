package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GeocoderControllerTest {

  @LocalServerPort
  Integer port;

  private final TestRestTemplate testRestTemplate = new TestRestTemplate();
  @MockBean
  private NominatimClient nominatimClient;

  // TODO Tests
  @Test
  void searchWhenNominationResponse() {
    when(nominatimClient.search(anyString()))
      .thenReturn(Optional.empty());

    ResponseEntity<NominatimPlace> response = testRestTemplate.
      getForEntity("http://localhost:" + port + "/geocoder/search?query=kubsu", NominatimPlace.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void search() {
    final  NominatimPlace testPlace = buildTestPlace();
    when(nominatimClient.search(anyString()))
      .thenReturn(Optional.of(testPlace));

    ResponseEntity<NominatimPlace> response = testRestTemplate.
      getForEntity("http://localhost:" + port + "/geocoder/search?query=kubsu", NominatimPlace.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    final NominatimPlace body = response.getBody();
    assertEquals(testPlace, body);
  }

  private static NominatimPlace buildTestPlace() {
    return new NominatimPlace(45.02036085, 39.03099994504268, "Кубанский государственный университет", "Университет");
  }
}
