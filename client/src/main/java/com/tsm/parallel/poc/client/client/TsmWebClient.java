package com.tsm.parallel.poc.client.client;

import com.tsm.parallel.poc.client.model.TsmDetailsInfo;
import com.tsm.parallel.poc.client.model.TsmDettails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class TsmWebClient {


    private final WebClient webClient;


    public TsmDettails call(TsmDetailsInfo detailInfo){

        log.info("Call to handler started for info: {}",detailInfo);

        return webClient.post().uri("http://localhost:9091/api/v1/va/detailsinfo")
                .bodyValue(detailInfo)
                .retrieve().bodyToMono(TsmDettails.class)
                .doOnSuccess(i -> log.info("Call to handler ended successfully"))
                .block();
    }

    public Mono<TsmDettails> callReactive(TsmDetailsInfo detailInfo){

        log.info("Call to handler started for info: {}",detailInfo);

        return webClient.post().uri("http://localhost:9091/api/v1/va/detailsinfo")
                .bodyValue(detailInfo)
                .retrieve().bodyToMono(TsmDettails.class)
                .doOnSuccess(i -> log.info("Call to handler ended successfully"));
    }
}
