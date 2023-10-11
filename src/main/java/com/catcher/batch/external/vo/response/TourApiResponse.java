package com.catcher.batch.external.vo.response;

import com.catcher.batch.annotation.CatcherJson;
import lombok.Getter;

import java.util.List;

@Getter
@CatcherJson(path = "response.body.items")
public class TourApiResponse {

    private List<TourApiItem> item;

//    private InnerTourAPiResponse response;
//
//    @Getter
//    public static class InnerTourAPiResponse {
//
//        private TourApiHeader header;
//
//        private TourApiBody body;
//    }
//
//    @Getter
//    public static class TourApiHeader {
//        private String resultCode;
//        private String resultMsg;
//    }
//
//    @Getter
//    public static class TourApiBody {
//
//        private TourApiItems items;
//        private Integer numOfRows;
//        private Integer pageNo;
//        private Integer totalCount;
//    }
//
//    @Getter
//    public static class TourApiItems {
//        private List<TourApiItem> item;
//    }

    /* TODO: JsonProperty 어노테이션 이용하여 적당한 변수명으로 바꾸기 */
    @Getter
    public static class TourApiItem {
        private String addr1;
        private String addr2;
        private String booktour;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentid;
        private String contenttypeid;
        private String createdtime;
        private String eventstartdate;
        private String eventenddate;
        private String firstimage;
        private String firstimage2;
        private String cpyrhtDivCd;
        private String mapx;
        private String mapy;
        private String mlevel;
        private String modifiedtime;
        private String areacode;
        private String sigungucode;
        private String tel;
        private String title;
    }
}
