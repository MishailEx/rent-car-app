package com.example.autoannouncement.controller;

import com.example.autoannouncement.model.ServerRequestAnn;
import com.example.autoannouncement.service.SearchService;
import com.example.autoentity.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HandlerSearch {

    private SearchService searchService;

    private Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    @Autowired
    public HandlerSearch(SearchService service) {
        this.searchService = service;
    }

    public Mono<ServerResponse> findModelByCatAndMark(ServerRequest serverRequest) {
        String category_id = serverRequest.queryParam("categoryId").orElse(null);
        String mark_id = serverRequest.queryParam("markId").orElse(null);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchService.findModelByCatAndMark(category_id, mark_id), Model.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAuto(ServerRequest serverRequest) {
        Mono<Auto> autoMono = serverRequest.bodyToMono(Auto.class);

        return autoMono.flatMap(rsl -> {
            return  ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(searchService.findByAuto(rsl), Auto.class)
                    .switchIfEmpty(notFound);
        });
    }

    public Mono<ServerResponse> findAllCategory(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(searchService.findAllCat(serverRequest), Category.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAllMark(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(searchService.findAllMark(serverRequest), Mark.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAnnByAuto(ServerRequest serverRequest) {

        Mono<ServerRequestAnn> ServerRequestAnnMono = serverRequest.bodyToMono(ServerRequestAnn.class);

        return ServerRequestAnnMono.flatMap(rsl -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(searchService.findAllByAnnByAuto(rsl.getCategory(),
                        rsl.getMark(), rsl.getModel(), rsl.getYear(), rsl.getPriceFrom(), rsl.getPriceTo()), Announcement.class)
                .switchIfEmpty(notFound));

    }
}
