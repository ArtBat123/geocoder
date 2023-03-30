package ru.kubsu.geocoder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.repository.AddressRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
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

  @Autowired
  private AddressRepository addressRepository;

  @BeforeEach
  void setUp() {
    addressRepository.deleteAll();
  }

  @Test
  void searchWhenNominationNotResponse() {
    final String query = "kubsu";
    when(nominatimClient.search(anyString()))
      .thenReturn(Optional.empty());

    ResponseEntity<Address> response = testRestTemplate.
      getForEntity("http://localhost:" + port + "/geocoder/search?query=" + query, Address.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void search() {
    final String query = "kubsu";
    final Address testAddress = buildTestAddress(query);
    when(nominatimClient.search(anyString()))
      .thenReturn(Optional.of(buildTestPlace()));

    ResponseEntity<Address> response = testRestTemplate.
      getForEntity("http://localhost:" + port + "/geocoder/search?query=" + query, Address.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    final Address body = response.getBody();
    assertEquals(testAddress, body);
  }

  @Test
  void reverseWhenNominationNotResponse() {
    final String latitude = "45.046910";
    final String longitude = "39.030416";
    when(nominatimClient.reverse(anyDouble(), anyDouble()))
      .thenReturn(Optional.empty());

    ResponseEntity<Address> response = testRestTemplate.
      getForEntity("http://localhost:" + port + "/geocoder/reverse?latitude=" + latitude + "&longitude=" + longitude, Address.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void reverse() {
    final String latitude = "45.02036085";
    final String longitude = "39.03099994504268";
    final Address testAddress = buildTestAddress(null);
    when(nominatimClient.reverse(anyDouble(), anyDouble()))
      .thenReturn(Optional.of(buildTestPlace()));

    ResponseEntity<Address> response = testRestTemplate.
      getForEntity("http://localhost:" + port + "/geocoder/reverse?latitude=" + latitude + "&longitude=" + longitude, Address.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    final Address body = response.getBody();
    assertEquals(testAddress, body);
  }

  private static NominatimPlace buildTestPlace() {
    return new NominatimPlace(45.02036085, 39.03099994504268, "Кубанский государственный университет", "Университет");
  }
  private static Address buildTestAddress(String query) {
    return Address.of(buildTestPlace(), query);
  }
}
