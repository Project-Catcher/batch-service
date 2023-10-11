package com.catcher.batch.core.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MovieApiResponse {

    private List<MovieItemDTO> response;

    @Getter
    public static class MovieItemDTO {
        private String rnum;
        private String rank;
        private String rankInten;
        private String rankOldAndNew;
        private String movieCd;
        private String movieNm;
        private String openDt;
        private String salesAmt;
        private String salesShare;
        private String salesInten;
        private String salesChange;
        private String salesAcc;
        private String audiCnt;
        private String audiInten;
        private String audiChange;
        private String audiAcc;
        private String scrnCnt;
        private String showCnt;
    }
}
