package com.galdovich.reactive.repository;

import com.galdovich.reactive.entity.Sport;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
public class SportCustomRepository {

    private final DatabaseClient client;

    public static void main(String[] args) throws InterruptedException {
        Flux.just(1, 2, 3, 4)
            .log()
            .doOnNext(e-> System.out.println("lol"))
            .map(i -> i * 2)
            .subscribeOn(Schedulers.parallel())
            .subscribe(System.out::println);

        System.out.println("Exiting main method");
        Thread.sleep(1000L);
    }


    public Mono<Sport> upsert(Sport sport) {
        return client.sql("""
                    INSERT INTO sports (natural_id, name)
                    VALUES (:id, :name)
                    ON CONFLICT (natural_id) DO UPDATE SET name = :name
                """)
            .bind("id", sport.getNaturalId())
            .bind("name", sport.getName())
            .then()
            .thenReturn(sport);
    }
}
