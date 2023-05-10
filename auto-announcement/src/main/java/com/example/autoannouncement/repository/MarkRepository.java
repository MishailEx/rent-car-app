package com.example.autoannouncement.repository;

import com.example.autoentity.model.Mark;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MarkRepository extends ReactiveCrudRepository<Mark, Long> {
}
