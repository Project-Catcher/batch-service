package com.catcher.batch.external;

import com.catcher.batch.external.vo.request.TourApiRequest;
import com.catcher.batch.external.vo.response.TourApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "TOUR-API-SERVICE", url = "http://apis.data.go.kr/${your_app_key}/KorService1")
public interface TourApiServiceExternalCallService {

    @GetMapping("/searchFestival1")
    TourApiResponse callTourList(@SpringQueryMap TourApiRequest request);
}
