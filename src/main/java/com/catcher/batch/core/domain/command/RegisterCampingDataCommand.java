package com.catcher.batch.core.domain.command;

import com.catcher.batch.core.dto.CampingApiResponse;
import com.catcher.batch.core.service.CampingService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RegisterCampingDataCommand implements Command<Void> {
    private final CampingService campingService;
    private final CampingApiResponse campingApiResponse;

    @Override
    public Void execute() {
        campingService.batch(campingApiResponse);
        return null;
    }
}
