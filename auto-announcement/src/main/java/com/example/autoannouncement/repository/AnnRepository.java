package com.example.autoannouncement.repository;

import com.example.autoentity.model.Announcement;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AnnRepository extends ReactiveCrudRepository<Announcement, Long> {

//    @Query("SELECT * FROM announcement WHERE (:autoId IS NULL OR auto_id = :autoId) AND (:price IS NULL OR price = :price)")
    @Query("SELECT * FROM announcement WHERE (:autoId IS NULL OR auto_id = :autoId) AND ((:priceFrom IS NULL AND :priceTo IS NULL) OR (price >= :priceFrom AND price <= :priceTo))")
    Flux<Announcement> findAllByAutoIdAndPrice(@Param("autoId") Long autoId, @Param("priceFrom") Integer priceFrom, @Param("priceTo") Integer priceTo);

}
