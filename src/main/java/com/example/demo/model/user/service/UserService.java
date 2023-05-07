package com.example.demo.model.user.service;

import com.example.demo.model.user.repository.ReactiveUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
//MapReactiveUserDetailsService  default example
public class UserService implements ReactiveUserDetailsService {

    private final ReactiveUserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return userRepository.findByEmail(username)
                .flatMap(data->{
                   return Mono.empty();
                });

    }
}
