package com.catcher.batch.core.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CampingApiResponse {
    private Response response;

    @Getter
    public static class Response {
        private Header header;
        private Body body;
    }

    @Getter
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    public static class Body {
        private Items items;
        private String numOfRows;
        private String pageNo;
        private String totalCount;
    }

    @Getter
    public static class Items {
        private List<CampingItemDTO> item;
    }

    // TODO 응답값 추후 필요한 것만 걸러내야 함
    @Getter
    public static class CampingItemDTO {
        private String contentId;
        private String facltNm;
        private String lineIntro;
        private String intro;
        private String allar;
        private String insrncAt;
        private String trsagntNo;
        private String bizrno;
        private String facltDivNm;
        private String mangeDivNm;
        private String mgcDiv;
        private String manageSttus;
        private String hvofBgnde;
        private String hvofEnddle;
        private String featureNm;
        private String induty;
        private String lctCl;
        private String doNm;
        private String sigunguNm;
        private String zipcode;
        private String addr1;
        private String addr2;
        private String mapX;
        private String mapY;
        private String direction;
        private String tel;
        private String homepage;
        private String resveUrl;
        private String resveCl;
        private String manageNmpr;
        private String gnrlSiteCo;
        private String autoSiteCo;
        private String glampSiteCo;
        private String caravSiteCo;
        private String indvdlCaravSiteCo;
        private String sitedStnc;
        private String siteMg1Width;
        private String siteMg2Width;
        private String siteMg3Width;
        private String siteMg1Vrticl;
        private String siteMg2Vrticl;
        private String siteMg3Vrticl;
        private String siteMg1Co;
        private String siteMg2Co;
        private String siteMg3Co;
        private String siteBottomCl1;
        private String siteBottomCl2;
        private String siteBottomCl3;
        private String siteBottomCl4;
        private String siteBottomCl5;
        private String tooltip;
        private String glampInnerFclty;
        private String caravInnerFclty;
        private String prmisnDe;
        private String operPdCl;
        private String operDeCl;
        private String trlerAcmpnyAt;
        private String caravAcmpnyAt;
        private String toiletCo;
        private String swrmCo;
        private String wtrplCo;
        private String brazierCl;
        private String sbrsCl;
        private String sbrsEtc;
        private String posblFcltyCl;
        private String posblFcltyEtc;
        private String clturEventAt;
        private String clturEvent;
        private String exprnProgrmAt;
        private String exprnProgrm;
        private String extshrCo;
        private String frprvtWrppCo;
        private String frprvtSandCo;
        private String fireSensorCo;
        private String themaEnvrnCl;
        private String eqpmnLendCl;
        private String animalCmgCl;
        private String tourEraCl;
        private String firstImageUrl;
        private String createdtime;
        private String modifiedtime;
    }
}
