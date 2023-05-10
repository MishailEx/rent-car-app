package com.example.autoannouncement.service;

import com.example.autoannouncement.repository.AnnRepository;
import com.example.autoannouncement.repository.AutoRepository;
import com.example.autoentity.model.Announcement;
import com.example.autoentity.model.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AnnService {

    private final AnnRepository annRepository;
    private final AutoRepository autoRepository;

    @Autowired
    public AnnService(AnnRepository annRepository, AutoRepository autoRepository) {
        this.annRepository = annRepository;
        this.autoRepository = autoRepository;
    }

    public Mono<Announcement> save(Announcement announcement) {
        Mono<Auto> autoMono = autoRepository.findById(1L);
        return autoMono.flatMap(a -> {
            announcement.setAutoId(a.getId());
            return annRepository.save(announcement);
        });
    }

    public Flux<Announcement> findAll() {
        return annRepository.findAll();
    }

    public Mono<Announcement> findById(Long id) {
        return annRepository.findById(id);
    }

    public Mono<Announcement> update(Announcement announcement, Long id) {

        if (announcement.getId() == null) announcement.setId(id);

        return annRepository.save(announcement);
    }

    public Mono<Void> delete(Long id) {
       return annRepository.deleteById(id);
    }


}
