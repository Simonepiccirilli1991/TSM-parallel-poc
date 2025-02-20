package com.tsm.parallel.poc.client.model.request;

import com.tsm.parallel.poc.client.model.TsmDetailsInfo;

import java.util.List;

public record TsmClientRequest(List<TsmDetailsInfo> detailsInfo) {
}
