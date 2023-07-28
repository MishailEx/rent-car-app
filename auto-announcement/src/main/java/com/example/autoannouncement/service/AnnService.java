package com.example.autoannouncement.service;

import com.example.autoannouncement.repository.FacadeRep;
import com.example.autoentity.model.Announcement;
import com.example.autoentity.model.Auto;
import com.example.autoentity.model.Mark;
import com.example.autoentity.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Service
public class AnnService {

    private final static Logger logger = LoggerFactory.getLogger(AnnService.class);

    private final FacadeRep facadeRep;

    @Autowired
    public AnnService(FacadeRep adapterRep) {
        this.facadeRep = adapterRep;
    }

    public Mono<Announcement> save(Announcement announcement) {
        Mono<Auto> autoMono = facadeRep.getAutoRepository().findById(announcement.getAutoId());
        return autoMono.flatMap(auto -> {

            Mono<Mark> markMono = facadeRep.getMarkRepository().findById(auto.getMarkId());
            Mono<Model> modelMono = facadeRep.getModelRepository().findById(auto.getModelId());

            return markMono.zipWith(modelMono).flatMap(zip -> {
                Mark mark = zip.getT1();
                Model model = zip.getT2();
                announcement.setName(mark.getName() + " " + model.getName() + " " + auto.getCarYear());
                announcement.setUserUUID(announcement.getUserUUID());
                Mono<Announcement> save = facadeRep.getAnnRepository().save(announcement);
                logger.info("пользователь - {} сохранил объявление с параметрами : {}", announcement.getUserUUID(), announcement.getName());
                return save;
            });
        });
    }

    public Flux<Announcement> findAll() {
        return facadeRep.getAnnRepository().findAll();
    }

    public Mono<Announcement> findById(Long id) {
        return facadeRep.getAnnRepository().findById(id);
    }

    public Mono<Announcement> update(Announcement announcement, Long id) {
        if (announcement.getId() == null) announcement.setId(id);
        return facadeRep.getAnnRepository().save(announcement);
    }

    public Mono<Void> delete(Long id) {
       return facadeRep.getAnnRepository().deleteById(id);
    }


}
