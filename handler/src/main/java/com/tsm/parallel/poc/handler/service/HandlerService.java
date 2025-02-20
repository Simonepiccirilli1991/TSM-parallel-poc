package com.tsm.parallel.poc.handler.service;

import com.tsm.parallel.poc.handler.model.HandlerRequest;
import com.tsm.parallel.poc.handler.model.HandlerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class HandlerService {


    public HandlerResponse handler(HandlerRequest request){
        log.info("Handler service started for request: {}",request);

        var nome = (ObjectUtils.isEmpty(request.nome())) ? "ajeje" : request.nome();
        var resp = new HandlerResponse(nome, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),120.00);

        try{
            Thread.sleep(1000);
        }catch (Exception e){
            log.error("Error during time out");
            throw new RuntimeException(e.getMessage());
        }
        log.info("Handler service ended successfully");
        return resp;
    }
}
