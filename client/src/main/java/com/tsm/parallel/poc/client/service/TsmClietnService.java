package com.tsm.parallel.poc.client.service;

import com.tsm.parallel.poc.client.client.TsmWebClient;
import com.tsm.parallel.poc.client.exception.TsmException;
import com.tsm.parallel.poc.client.model.TsmDetailsInfo;
import com.tsm.parallel.poc.client.model.TsmDettails;
import com.tsm.parallel.poc.client.model.request.TsmClientRequest;
import com.tsm.parallel.poc.client.model.response.TsmClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class TsmClietnService {

    private final TsmWebClient tsmWebClient;

    // classico
    public Mono<ResponseEntity<TsmClientResponse>> tsmClientOrchestrator(TsmClientRequest request){

        log.info("tsmClientOrchestrator service started with raw request: {}",request);

        var iResp = clienteMultiple(request.detailsInfo());

        log.info("tsmClientOrchestrator service ended successfully");
        return Mono.just(ResponseEntity.ok(iResp));
    }

    // reattivo
    public Mono<ResponseEntity<TsmClientResponse>> tsmClientOrchestratorReactive(TsmClientRequest request){

        log.info("tsmClientOrchestratorReactive service started with raw request: {}",request);

        var iResp = reactiveMultipleTest(request.detailsInfo());

        log.info("tsmClientOrchestratorReactive service ended successfully");
        return Mono.just(ResponseEntity.ok(iResp));
    }


    // parallelo su virtuale
    private TsmClientResponse clienteMultiple(List<TsmDetailsInfo> detailsInfo){
            log.info("ClienteMultiple case");
            var timeStart = System.currentTimeMillis();
            var executor = Executors.newVirtualThreadPerTaskExecutor();
            List<Future<TsmDettails>> futuresInfo = new ArrayList<>();

            for(var details : detailsInfo){
                futuresInfo.add(executor.submit(() ->
                        tsmWebClient.call(details)
                ));
            }
        // Collect all responses
        try {
            List<TsmDettails> responses = new ArrayList<>();
            for (var future : futuresInfo) {
                responses.add(future.get());
            }

            executor.shutdown();
            var timeEnd = System.currentTimeMillis() - timeStart;
            log.info("Time for complete cicle is: {}",timeEnd);
            return new TsmClientResponse(responses);

        }catch (Exception e){
            log.error("Error on clienteMultiple with err: {}",e.getMessage());
            throw new TsmException("Error on getting future",e.getMessage());
        }
    }
    // flux reactive
    private TsmClientResponse reactiveMultipleTest(List<TsmDetailsInfo> detailsInfo){
        log.info("ReactiveMultipleTest service started");

        var timeStart = System.currentTimeMillis();

        var iResp = Flux.fromIterable(detailsInfo)
                // Run calls in parallel on a bounded elastic scheduler
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(detail ->
                        tsmWebClient.callReactive(detail)
                )
                // Merge parallel results back into a sequential Flux
                .sequential()
                // Collect all items into a List and block until complete
                .collectList()
                .block();

        var resp = new TsmClientResponse(iResp);
        log.info("Time for complete reactive cicle is: {}",(System.currentTimeMillis() - timeStart));
        return resp;
    }

}
