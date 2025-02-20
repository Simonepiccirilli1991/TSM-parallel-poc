package com.tsm.parallel.poc.handler.controller;

import com.tsm.parallel.poc.handler.model.HandlerRequest;
import com.tsm.parallel.poc.handler.model.HandlerResponse;
import com.tsm.parallel.poc.handler.service.HandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/va")
@RequiredArgsConstructor
public class HandlerController {

    private final HandlerService handlerService;


    @PostMapping("detailsinfo")
    public ResponseEntity<HandlerResponse> handlerController(@RequestBody HandlerRequest request){
        return ResponseEntity.ok(handlerService.handler(request));
    }
}
