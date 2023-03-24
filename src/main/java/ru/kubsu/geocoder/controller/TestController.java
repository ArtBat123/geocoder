package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kubsu.geocoder.model.Test;
import ru.kubsu.geocoder.service.TestService;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 */
@RestController
@RequestMapping("tests")
public class TestController {

    private final TestService service;

    @Autowired
    public TestController(final TestService service) {
      this.service = service;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Test getTest(@PathVariable final Integer id,
                        @RequestParam final String name) {
        return service.build(id, name);
    }

    @GetMapping(value = "/save", produces = APPLICATION_JSON_VALUE)
    public void save(@RequestParam final String name) {
        service.save(name);
    }
    @GetMapping(value = "/load/{name}", produces = APPLICATION_JSON_VALUE)
    public Test load(@PathVariable final String name) {
        return service.load(name);
    }

    @GetMapping("/not_found")
    public ResponseEntity<Object> notFound() {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
