package com.galdovich.reactive.service;

import com.galdovich.reactive.entity.Sport;
import com.galdovich.reactive.repository.SportCustomRepository;
import com.galdovich.reactive.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;
    private final SportCustomRepository customRepository;

    public Mono<Sport> createSport(String name) {
        String id = UUID.randomUUID().toString();
        Sport sport = new Sport(id, name);

        return sportRepository.existsByName(name)
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalStateException("Sport with name already exists"));
                } else {
                    return customRepository.upsert(sport);
                }
            });
    }

    public Flux<Sport> saveAll(Flux<Sport> sports) {
        return sports.flatMap(sport ->
            customRepository.upsert(sport).thenReturn(sport)
        );
    }

    public Mono<Sport> findByIdentifier(String id) {
        return sportRepository.findById(id);
    }

    public Flux<Sport> findByName(String name) {
        return sportRepository.findByNameContainingIgnoreCase(name);
    }
}
