package com.tsm.parallel.poc.client.model.response;

import com.tsm.parallel.poc.client.model.TsmDettails;

import java.util.List;

public record TsmClientResponse(List<TsmDettails> dettails) {
}
