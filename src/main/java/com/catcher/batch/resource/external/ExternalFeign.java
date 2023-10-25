package com.catcher.batch.resource.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name = "test", url = "runtime")
public interface ExternalFeign {
    @GetMapping
    String getInfo(URI url);

}
