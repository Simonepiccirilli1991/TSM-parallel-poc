package com.tsm.parallel.poc.client.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class TsmExceptionHandler {


    @ExceptionHandler(TsmException.class)
    public Mono<ResponseEntity<TsmException>> exceptionHandler(TsmException e){
        return Mono.just(ResponseEntity.internalServerError().body(e));
    }
}
