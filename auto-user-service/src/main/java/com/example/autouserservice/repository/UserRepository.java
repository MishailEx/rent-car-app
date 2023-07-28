package com.example.autouserservice.repository;

import com.example.autouserservice.model.UserImplUserDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<UserImplUserDetails, String> {

    Mono<UserImplUserDetails> findByEmail(String email);


}
