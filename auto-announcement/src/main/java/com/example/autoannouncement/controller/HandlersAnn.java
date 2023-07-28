package com.example.autoannouncement.controller;


import com.example.autoannouncement.service.AnnService;
import com.example.autoentity.model.Announcement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class HandlersAnn {
    private Logger logger = LoggerFactory.getLogger(HandlersAnn.class);
    private final AnnService annService;
    private final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    @Autowired
    public HandlersAnn(AnnService annService) {
        this.annService = annService;
    }

    public Mono<String> getStringFromMultipartData(Part part) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return DataBufferUtils.write(part.content(), outputStream)
                .then(Mono.defer(() -> {
                    byte[] data = outputStream.toByteArray();
                    String result = new String(data, StandardCharsets.UTF_8);
                    return Mono.just(result);
                }));
    }

    private MultiValueMap<String, Part> buildFormData(List<Part> parts, long id) {
        MultiValueMap<String, Part> formData = new LinkedMultiValueMap<>();

        for (int i = 0; i < parts.size(); i++) {
            Part part = parts.get(i);
            formData.add("images[]", part);
        }

        formData.add("id", new Part() {
            @Override
            public String name() {
                return "id";
            }

            @Override
            public HttpHeaders headers() {
                HttpHeaders idHeaders = new HttpHeaders();
                idHeaders.setContentType(MediaType.TEXT_PLAIN);
                idHeaders.setContentDispositionFormData("id", Long.toString(id));
                return idHeaders;
            }

            @Override
            public Flux<DataBuffer> content() {
                String idValue = Long.toString(id);
                byte[] bytes = idValue.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);
                return Flux.just(buffer);
            }
        });

        return formData;
    }

    public Mono<ServerResponse> save(@NotNull ServerRequest serverRequest) {
        WebClient webClient = WebClient.create();

        Mono<MultiValueMap<String, Part>> multiValueMapMono = serverRequest.multipartData();
        return multiValueMapMono.flatMap(file -> {
            List<Part> parts = file.get("images[]");

            Mono<String> autoIdMono = getStringFromMultipartData(file.get("autoId").get(0));
            Mono<String> priceMono = getStringFromMultipartData(file.get("price").get(0));
            Mono<String> descriptionMono = getStringFromMultipartData(file.get("description").get(0));
            Mono<String> userUUIDMono = getStringFromMultipartData(file.get("userUUID").get(0));
            return Mono.zip(autoIdMono, priceMono, descriptionMono, userUUIDMono).flatMap(tuple -> {
                String t1 = tuple.getT1();
                String t2 = tuple.getT2();
                String t3 = tuple.getT3();
                String t4 = tuple.getT4();
                Mono<Announcement> announcementMono = annService.save(new Announcement(Long.parseLong(t1), Integer.parseInt(t2), t3, t4));
                logger.info("Пользователь - {}, добавил объявление с параметрами({},{},{})", t4, t1, t2, t3);
                return announcementMono.flatMap(ann -> webClient.post()
                        .uri("http://localhost:8235/add")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(buildFormData(parts, ann.getId())))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, List<String>>>() {})
                        .onErrorResume(throwable -> {
                            String errorMessage = "Не удалось загрузить картинки. Пожалуйста, попробуйте еще раз.";
                            return Mono.just(Collections.singletonMap("error", Collections.singletonList(errorMessage)));
                        })
                        .flatMap(resp -> {
                            if (resp != null && !resp.containsKey("error")) {
                                List<String> respUrls = resp.get("urls");
                                List<String> images = ann.getImages();
                                images.addAll(respUrls);
                                ann.setImages(images);
                                return annService.save(ann).flatMap(rsl -> ServerResponse.ok().build());
                            }
                            String errorMessage = resp.get("error").get(0);
                            System.out.println(errorMessage);
                            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Collections.singletonMap("error", errorMessage));
                        }));
            });
        });
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(annService.findAll(), Announcement.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(annService.findById(id), Announcement.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Announcement> announcementMono = serverRequest.bodyToMono(Announcement.class);
        return announcementMono.flatMap(ann ->
                        ServerResponse
                                .status(HttpStatus.CREATED)
                                .body(annService.update(ann, id), Announcement.class)
                                .doOnNext(updateAnn -> {
                                    logger.info("объявление id - {} j, обновлено с параметрами: {}", id, ann);
                                })
                                .doOnError(error -> {
                                    logger.error("ошибка в объявлении id - {} j, не обновлено с параметрами: {}", id, ann);
                                }))
                .switchIfEmpty(notFound);

    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        return ServerResponse.status(HttpStatus.NO_CONTENT)
                .build(annService.delete(id)
                        .doOnSuccess(updateAnn -> {
                            logger.info("объявление с id - {} удаленно", id);
                        })
                        .doOnError(error -> {
                            logger.error("не удалось удалить объявление с id - {}", id);
                        }))
                .switchIfEmpty(notFound);
    }
}
