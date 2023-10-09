package com.catcher.batch.job.config;

import com.catcher.batch.external.TourApiServiceExternalCallService;
import com.catcher.batch.external.vo.request.TourApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TourApiJob {

    private final TourApiServiceExternalCallService tourApiServiceExternalCallService;

    /* TODO: Spring batch 이용하도록 변경? */
    @Scheduled(fixedDelay = 1000000) //TODO: 시간대 정해서 하루에 1번으로 변경
    public void tourApiJob() {

        final var response = tourApiServiceExternalCallService.callTourList(new TourApiRequest());
    }

}
