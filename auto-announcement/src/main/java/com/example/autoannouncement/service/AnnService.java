package com.example.autoannouncement.service;

import com.example.autoannouncement.repository.AdapterRep;
import com.example.autoentity.model.Announcement;
import com.example.autoentity.model.Auto;
import com.example.autoentity.model.Mark;
import com.example.autoentity.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AnnService {

    private final AdapterRep adapterRep;

    @Autowired
    public AnnService(AdapterRep adapterRep) {
        this.adapterRep = adapterRep;
    }

    public Mono<Announcement> save(Announcement announcement) {
        Mono<Auto> autoMono = adapterRep.getAutoRepository().findById(announcement.getAutoId());
        return autoMono.flatMap(auto -> {

            Mono<Mark> markMono = adapterRep.getMarkRepository().findById(auto.getMarkId());
            Mono<Model> modelMono = adapterRep.getModelRepository().findById(auto.getModelId());

            return markMono.zipWith(modelMono).flatMap(zip -> {
                Mark mark = zip.getT1();
                Model model = zip.getT2();
                announcement.setName(mark.getName() + " " + model.getName() + " " + auto.getCarYear());
                announcement.setUserUUID(announcement.getUserUUID());
                return adapterRep.getAnnRepository().save(announcement);
            });
        });
    }

    public Flux<Announcement> findAll() {
        return adapterRep.getAnnRepository().findAll();
    }

    public Mono<Announcement> findById(Long id) {
        return adapterRep.getAnnRepository().findById(id);
    }

    public Mono<Announcement> update(Announcement announcement, Long id) {

        if (announcement.getId() == null) announcement.setId(id);

        return adapterRep.getAnnRepository().save(announcement);
    }

    public Mono<Void> delete(Long id) {
       return adapterRep.getAnnRepository().deleteById(id);
    }


}
