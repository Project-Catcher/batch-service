package com.catcher.batch.infrastructure.external;

import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.infrastructure.external.response.InternalAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catcher-service", url = "${url.catcher-service}")
public interface CatcherServiceFeignClient {

    @GetMapping("/internal/address/{query}")
    CommonResponse<InternalAddressResponse> getAddressByQuery(@PathVariable("query") String query);
}