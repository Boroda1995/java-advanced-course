package com.galdovich.reactive.controller;

import com.galdovich.reactive.entity.Sport;
import com.galdovich.reactive.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/sport")
public class SportRestController {

    @Autowired
    public SportService service;

    @GetMapping(value = "/{identifier}")
    public Mono<ResponseEntity<Sport>> getByIdentifier(@PathVariable(name = "identifier") String identifier) {
        return service.findByIdentifier(identifier)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
