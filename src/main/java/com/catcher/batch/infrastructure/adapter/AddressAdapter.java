package com.catcher.batch.infrastructure.adapter;


import com.catcher.batch.common.response.CommonResponse;
import com.catcher.batch.core.port.AddressPort;
import com.catcher.batch.infrastructure.external.CatcherServiceFeignClient;
import com.catcher.batch.infrastructure.external.response.InternalAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressAdapter implements AddressPort {

    private final CatcherServiceFeignClient catcherServiceFeignClient;

    @Override
    public Optional<String> getAreaCodeByQuery(final String query) {
        // TODO 덕주님 error decorder 처리해주세요.
        try {
            final CommonResponse<InternalAddressResponse> response =
                    catcherServiceFeignClient.getAddressByQuery(query);

            if (!response.isSuccess()) {
                return Optional.empty();
            }

            return Optional.of(response.getResult().getAreaCode());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}