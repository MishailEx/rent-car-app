package com.example.autoannouncement.service;

import com.example.autoannouncement.repository.FacadeRep;
import com.example.autoentity.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SearchService {

   private FacadeRep facadeRep;

    public SearchService(FacadeRep facadeRep) {
        this.facadeRep = facadeRep;
    }

    public Flux<Category> findAllCat(ServerRequest serverRequest) {
        return facadeRep.getCategoryRepository().findAll();
    }

    public Flux<Mark> findAllMark(ServerRequest serverRequest) {
        return facadeRep.getMarkRepository().findAll();
    }

    public Flux<Model> findModelByCatAndMark(String catId, String markId) {
        var l = catId != null ? Long.parseLong(catId) : null;
        var l1 = markId != null ? Long.parseLong(markId) : null;
        return facadeRep.getModelRepository().findAllByCategoryIdAndMarkId(l, l1);
    }

    public Flux<Auto> findByAuto(Auto rsl) {
        return facadeRep.getAutoRepository().findAllByCategoryIdAndMarkIdAndModelIdAndYear(rsl.getCategoryId(),
                rsl.getMarkId(), rsl.getModelId(), rsl.getCarYear());
    }

    public Mono<Auto> findByAutoForAddAnn(Auto rsl) {
        return facadeRep.getAutoRepository().findByCategoryIdAndMarkIdAndModelIdAndCarYear(rsl.getCategoryId(),
                rsl.getMarkId(), rsl.getModelId(), rsl.getCarYear()).switchIfEmpty(facadeRep.getAutoRepository().save(rsl));
    }

    public Flux<Announcement> findAllByAnnByAuto(Long cat, Long mark, Long model, Integer year, Integer priceFrom, Integer priceTo) {

        Flux<Auto> autoFlux = facadeRep.getAutoRepository().findAllByCategoryIdAndMarkIdAndModelIdAndYear(cat,
                mark, model, year);

        return autoFlux.flatMap(auto -> facadeRep.getAnnRepository().findAllByAutoIdAndPrice(auto.getId(), priceFrom, priceTo));
    }
}
