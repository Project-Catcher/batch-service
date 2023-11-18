package com.catcher.batch.resource.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "test", url = "runtime")
public interface ExternalFeign {
    @GetMapping
    String getInfo(URI url);

    @GetMapping
    String getInfoWithHeader(@RequestHeader("Authorization") String authorizationHeader, URI url);
}
