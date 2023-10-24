package com.catcher.batch.resource.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@FeignClient(name = "test", url = "runtime")
public interface ExternalFeign {
    @GetMapping
    String getInfo(URI url);

}
