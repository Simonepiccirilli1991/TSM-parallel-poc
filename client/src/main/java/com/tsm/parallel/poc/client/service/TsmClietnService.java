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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class TsmClietnService {

    private final TsmWebClient tsmWebClient;

    public Mono<ResponseEntity<TsmClientResponse>> tsmClientOrchestrator(TsmClientRequest request){

        log.info("tsmClientOrchestrator service started with raw request: {}",request);

        var iResp = clienteMultiple(request.detailsInfo());

        log.info("tsmClientOrchestrator service ended successfully");
        return Mono.just(ResponseEntity.ok(iResp));
    }


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
}
