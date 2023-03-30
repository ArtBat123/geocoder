package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.service.AddressService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 */
@RestController
@RequestMapping("geocoder")
public class GeocoderController {

  private final AddressService addressService;
  @Autowired
  public GeocoderController(final AddressService addressService) {
    this.addressService = addressService;
  }
  @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Address> search(@RequestParam final String query) {
    return addressService.search(query).map(p -> ResponseEntity.status(HttpStatus.OK).body(p))
      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
  @GetMapping(value = "/reverse", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Address> reverse(@RequestParam final Double latitude,
                                         @RequestParam final Double longitude) {
    return addressService.reverse(latitude, longitude).map(p -> ResponseEntity.status(HttpStatus.OK).body(p))
      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }
}
