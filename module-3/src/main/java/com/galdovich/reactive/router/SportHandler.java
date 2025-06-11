package com.galdovich.reactive.router;

import com.galdovich.reactive.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SportHandler {

    @Autowired
    private SportService service;

    public Mono<ServerResponse> createSport(ServerRequest request) {
        String sportName = request.pathVariable("sportname");

        return service.createSport(sportName)
            .flatMap(sport -> ServerResponse.status(HttpStatus.CREATED).bodyValue(sport))
            .onErrorResume(IllegalStateException.class, ex ->
                ServerResponse.status(HttpStatus.CONFLICT).bodyValue(ex.getMessage())
            );
    }

    public Mono<ServerResponse> searchSports(ServerRequest request) {
        String query = request.queryParam("q").orElse("");
        return service.findByName(query)
            .collectList()
            .flatMap(sports -> ServerResponse.ok().bodyValue(sports));
    }

}
