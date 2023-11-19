package com.catcher.batch.core.port;

import java.util.Optional;

public interface AddressPort {

    Optional<String> getAreaCodeByQuery(String query);
}
