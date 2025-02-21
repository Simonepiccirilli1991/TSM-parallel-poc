package com.tsm.parallel.poc.client.controller;

import com.tsm.parallel.poc.client.model.request.TsmClientRequest;
import com.tsm.parallel.poc.client.model.response.TsmClientResponse;
import com.tsm.parallel.poc.client.service.TsmClietnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/client")
@RequiredArgsConstructor
public class TsmClientController {

    private final TsmClietnService tsmClietnService;

    @PostMapping("details")
    public ResponseEntity<TsmClientResponse> details(@RequestBody TsmClientRequest request){
        return ResponseEntity.ok(tsmClietnService.tsmClientOrchestrator(request));
    }

    @PostMapping("reactive/details")
    public Mono<ResponseEntity<TsmClientResponse>> detailsReactive(@RequestBody TsmClientRequest request){
        return Mono.just(ResponseEntity.ok(tsmClietnService.tsmClientOrchestratorReactive(request)));
    }
}
