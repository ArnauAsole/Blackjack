package com.example.blackjack.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ProblemDetail> handle(ResponseStatusException ex){
        return Mono.just(ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason()));
    }

    @ExceptionHandler(Throwable.class)
    public Mono<ProblemDetail> handle(Throwable ex){
        return Mono.just(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
