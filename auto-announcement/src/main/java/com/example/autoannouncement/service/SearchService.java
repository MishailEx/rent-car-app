package com.example.autoannouncement.service;

import com.example.autoannouncement.repository.*;
import com.example.autoentity.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SearchService {

    private AnnRepository annRepository;
    private AutoRepository autoRepository;
    private CategoryRepository categoryRepository;
    private ModelRepository modelRepository;
    private MarkRepository markRepository;

    @Autowired
    public SearchService(AnnRepository annRepository,
                         AutoRepository autoRepository,
                         CategoryRepository categoryRepository,
                         ModelRepository modelRepository,
                         MarkRepository markRepository) {
        this.annRepository = annRepository;
        this.autoRepository = autoRepository;
        this.categoryRepository = categoryRepository;
        this.modelRepository = modelRepository;
        this.markRepository = markRepository;
    }

    public Flux<Category> findAllCat(ServerRequest serverRequest) {
        return categoryRepository.findAll();
    }

    public Flux<Mark> findAllMark(ServerRequest serverRequest) {
        return markRepository.findAll();
    }

    public Flux<Model> findModelByCatAndMark(String catId, String markId) {
        var l = catId != null ? Long.parseLong(catId) : null;
        var l1 = markId != null ? Long.parseLong(markId) : null;
        return modelRepository.findAllByCategoryIdAndMarkId(l, l1);
    }

    public Flux<Auto> findByAuto(Auto rsl) {
        return autoRepository.findAllByCategoryIdAndMarkIdAndModelIdAndYear(rsl.getCategoryId(),
                rsl.getMarkId(), rsl.getModelId(), rsl.getCarYear());
    }

    public Mono<Auto> findByAutoForAddAnn(Auto rsl) {
        return autoRepository.findByCategoryIdAndMarkIdAndModelIdAndCarYear(rsl.getCategoryId(),
                rsl.getMarkId(), rsl.getModelId(), rsl.getCarYear()).switchIfEmpty(autoRepository.save(rsl));
    }

    public Flux<Announcement> findAllByAnnByAuto(Long cat, Long mark, Long model, Integer year, Integer priceFrom, Integer priceTo) {

        Flux<Auto> autoFlux = autoRepository.findAllByCategoryIdAndMarkIdAndModelIdAndYear(cat,
                mark, model, year);

        return autoFlux.flatMap(auto -> annRepository.findAllByAutoIdAndPrice(auto.getId(), priceFrom, priceTo));
    }
}
