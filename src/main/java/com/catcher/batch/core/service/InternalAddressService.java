package com.catcher.batch.core.service;

import com.catcher.batch.infrastructure.adapter.AddressAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternalAddressService {

    private final AddressAdapter addressAdapter;

    public Optional<String> getAreaCodeByQuery(final String query) {
        return addressAdapter.getAreaCodeByQuery(query);
    }
}
