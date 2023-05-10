package com.example.autoannouncement.repository;

import com.example.autoentity.model.Auto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AutoRepository extends ReactiveCrudRepository<Auto, Long> {

    @Query("SELECT * FROM auto WHERE (:categoryId IS NULL OR category_id = :categoryId) " +
            "AND (:markId IS NULL OR mark_id = :markId) AND (:modelId IS NULL OR model_id = :modelId) " +
            "AND (:carYear IS NULL OR car_year = :carYear)")
    Flux<Auto> findAllByCategoryIdAndMarkIdAndModelIdAndYear(@Param("categoryId") Long catId,
                                                          @Param("markId") Long markId,
                                                          @Param("modelId") Long modelId,
                                                          @Param("carYear") Integer year);
}
