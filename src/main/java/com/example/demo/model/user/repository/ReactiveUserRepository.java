package com.example.demo.model.user.repository;


import com.example.demo.model.user.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveUserRepository extends ReactiveMongoRepository<User,String>{
    Mono<User> findByEmail(String email);
}
