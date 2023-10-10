package com.catcher.batch.external.vo.request;

import lombok.Getter;

/* TODO: 하드코딩된 값 바꾸기 */

@Getter
public class TourApiRequest {

    private Integer numOfRows = 100;

    private Integer pageNo = 1;

    private String MobileOS = "ETC";

    private String MobileApp = "AppTest";

    private String _type = "json";

    private String listYN = "Y";

    private String arrange = "A";

    private String eventStartDate = "20230901";

    private String serviceKey = "your_service_key";
}
