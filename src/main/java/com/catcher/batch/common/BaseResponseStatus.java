package com.catcher.batch.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    // Batch
    FAIL_LOCATION_SERVER(1004, "주소 검색 서버 연결에 실패했습니다"),
    DATA_NOT_FOUND(1001, "해당 데이터를 찾을 수 없습니다.");

    private final int code;
    private final String message;

    BaseResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
