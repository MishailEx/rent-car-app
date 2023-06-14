package com.example.autoannouncement.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Routers {

    private HandlersAnn handlersAnn;

    private HandlerSearch handlerSearch;


    public Routers(HandlersAnn handlersAnn, HandlerSearch handlerSearch) {
        this.handlersAnn = handlersAnn;
        this.handlerSearch = handlerSearch;
    }


    @Bean
    public RouterFunction<ServerResponse> adRoutes() {

        return RouterFunctions
                .nest(path("/api/ann"),
                        nest(accept(MediaType.APPLICATION_JSON),
                                route(GET("/all"), handlersAnn::findAll)
                                        .andRoute(GET("/{id}"), handlersAnn::findById)
                                        .andRoute(POST("/add"), handlersAnn::save)
                                        .andRoute(PUT("/update/{id}"), handlersAnn::update)
                                        .andRoute(DELETE("/delete/{id}"), handlersAnn::delete)))

                .andNest(path("/api/search"),
                        nest(accept(MediaType.APPLICATION_JSON),
                                route(GET("/model"), handlerSearch::findModelByCatAndMark)
                                        .andRoute(POST("/auto"), handlerSearch::findAuto)
                                        .andRoute(POST("/autoForAddAnn"), handlerSearch::findAutoForAddAnn)
                                        .andRoute(GET("/category"), handlerSearch::findAllCategory)
                                        .andRoute(GET("/mark"), handlerSearch::findAllMark)
                                        .andRoute(POST("/ann"), handlerSearch::findAnnByAuto)));


    }
}
