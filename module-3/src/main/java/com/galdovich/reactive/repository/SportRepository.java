package com.galdovich.reactive.repository;

import com.galdovich.reactive.entity.Sport;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SportRepository extends ReactiveCrudRepository<Sport, String> {

    Mono<Sport> findById(String id);

    Flux<Sport> findByNameContainingIgnoreCase(String name);

    Mono<Boolean> existsByName(String name);
}

