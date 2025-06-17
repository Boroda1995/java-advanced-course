package com.galdovich.reactive.etl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.galdovich.reactive.entity.Sport;
import com.galdovich.reactive.service.SportService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class SportSetup {

    private final WebClient webClient;
    private final SportService sportService;

    @Value(value = "${client.rakuten.appId}")
    private String applicationId;
    @Value(value = "${client.rakuten.genreId}")
    private String genreId;
    @Value(value = "${client.rakuten.keyword}")
    private String keyword;

    @Autowired
    public SportSetup(WebClient webClient, SportService sportService) {
        this.webClient = webClient;
        this.sportService = sportService;
    }

    @PostConstruct
    public void fetchAndSaveSports() {
        webClient.get()
            .uri(uriBuilder -> uriBuilder.path("/services/api/IchibaItem/Search/20170706")
                .queryParam("applicationId", applicationId)
                .queryParam("keyword", keyword)
                .queryParam("genreId", genreId)
                .build())
            .retrieve()
            .bodyToMono(JsonNode.class)
            .doOnSuccess(response -> log.info("Response is success: " + response))
            .doOnError(error -> log.info("Response is failed: " + error.getMessage()))
            .flatMapMany(jsonNode -> {
                ArrayNode itemsArray = (ArrayNode) jsonNode.path("Items");
                return Flux.fromIterable(itemsArray)
                    .map(itemNode -> {
                        JsonNode item = itemNode.path("Item");
                        String itemName = item.path("itemName").asText();
                        String itemCode = item.path("itemCode").asText();
                        return Sport.builder()
                            .naturalId(itemCode)
                            .name(itemName)
                            .build();
                    });
            })
            .limitRate(20)
            .doOnRequest(n -> log.info("Requesting next {} elements", n))
            .doOnNext(item -> log.info("Processing item: {}", item.getName()))
            .as(sportService::saveAll)
            .subscribe(
                saved -> log.info("Saved: " + saved.getName()),
                error -> log.error("Error saving sports: " + error.getMessage())
            );
    }
}

