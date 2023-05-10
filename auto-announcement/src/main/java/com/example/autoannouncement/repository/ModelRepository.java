package com.example.autoannouncement.repository;

import com.example.autoentity.model.Model;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ModelRepository extends ReactiveCrudRepository<Model, Long> {

    @Query("SELECT * FROM model WHERE (:categoryId IS NULL OR category_id = :categoryId) AND (:markId IS NULL OR mark_id = :markId)")
    Flux<Model> findAllByCategoryIdAndMarkId(@Param("categoryId") Long categoryId, @Param("markId") Long markId);

}
