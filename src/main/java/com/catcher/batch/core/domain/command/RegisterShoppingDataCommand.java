package com.catcher.batch.core.domain.command;

import com.catcher.batch.core.dto.ShoppingApiResponse;
import com.catcher.batch.core.service.ShoppingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterShoppingDataCommand implements Command<Void> {
    private final ShoppingService shoppingService;
    private final ShoppingApiResponse shoppingApiResponse;

    @Override
    public Void execute() {
        shoppingService.batch(shoppingApiResponse);
        return null;
    }
}
