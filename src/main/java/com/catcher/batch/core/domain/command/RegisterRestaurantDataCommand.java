package com.catcher.batch.core.domain.command;

import com.catcher.batch.core.dto.RestaurantApiResponse;
import com.catcher.batch.core.service.RestaurantService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterRestaurantDataCommand implements Command<Void> {
    private final RestaurantService restaurantService;
    private final RestaurantApiResponse restaurantApiResponse;

    @Override
    public Void execute() {
        restaurantService.batch(restaurantApiResponse);
        return null;
    }
}
