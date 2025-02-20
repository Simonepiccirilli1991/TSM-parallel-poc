package com.tsm.parallel.poc.client.exception;

import lombok.Data;

@Data
public class TsmException extends RuntimeException{

    private String msg;
    private String error;

    public TsmException(String msg, String error) {
        this.msg = msg;
        this.error = error;
    }
}
