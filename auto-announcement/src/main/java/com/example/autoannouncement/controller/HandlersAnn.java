package com.example.autoannouncement.controller;


import com.example.autoannouncement.repository.AutoRepository;
import com.example.autoannouncement.repository.CategoryRepository;
import com.example.autoannouncement.service.AnnService;
import com.example.autoentity.model.Announcement;
import com.example.autoentity.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HandlersAnn {

    private AnnService annService;
    private AutoRepository autoRepository;

    private CategoryRepository categoryRepository;
    private Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    @Autowired
    public HandlersAnn(AnnService annService, AutoRepository autoRepository, CategoryRepository categoryRepository) {
        this.annService = annService;
        this.autoRepository = autoRepository;
        this.categoryRepository = categoryRepository;
    }

    public Mono<ServerResponse> findByIdCat(ServerRequest serverRequest) {
        return ServerResponse.ok().body(categoryRepository.findById(1L), Category.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {

        Mono<Announcement> announcementMono = serverRequest.bodyToMono(Announcement.class);
        return announcementMono.flatMap( ann ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .body(annService.save(ann), Announcement.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(annService.findAll(), Announcement.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
//        UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(annService.findById(id), Announcement.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
//        UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Announcement> announcementMono = serverRequest.bodyToMono(Announcement.class);
        return announcementMono.flatMap( ann ->
                    ServerResponse
                            .status(HttpStatus.CREATED)
                            .body(annService.update(ann, id), Announcement.class))
                .switchIfEmpty(notFound);

    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
//        UUID id = UUID.fromString(serverRequest.pathVariable("id"));
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        return ServerResponse.status(HttpStatus.NO_CONTENT)
                .build(annService.delete(id))
                .switchIfEmpty(notFound);
    }
}
