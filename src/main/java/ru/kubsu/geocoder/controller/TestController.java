package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.dto.NominatimPlace;
import ru.kubsu.geocoder.model.Test;
import ru.kubsu.geocoder.service.TestService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("tests")
public class TestController {

    private TestService service;
    private NominatimClient nominatimClient;

    @Autowired
    public TestController(TestService service, NominatimClient nominatimClient) {
      this.service = service;
      this.nominatimClient = nominatimClient;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Test getTest(@PathVariable Integer id,
                        @RequestParam String name) {
        return service.build(id, name);
    }

    @GetMapping(value = "/save", produces = APPLICATION_JSON_VALUE)
    public void save(@RequestParam String name) {
        service.save(name);
    }
    @GetMapping(value = "/load/{name}", produces = APPLICATION_JSON_VALUE)
    public Test load(@PathVariable String name) {
        return service.load(name);
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public NominatimPlace search(@RequestParam String query) {
      return nominatimClient.search(query, "json").get(0);
    }
    @GetMapping(value = "/reverse", produces = APPLICATION_JSON_VALUE)
    public NominatimPlace reverse(@RequestParam String latitude,
                                  @RequestParam String longitude) {
      return nominatimClient.reverse(latitude, longitude, "json");
    }

    @GetMapping(value = "/not_found")
    public ResponseEntity<Object> notFound() {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();//
    }
}
