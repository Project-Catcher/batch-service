package com.catcher.batch.common.utils;

import com.catcher.batch.common.response.CommonResponse;

public class ApiUtils {
    public static <T> CommonResponse<T> success(int code, T result) {
        return new CommonResponse<>(code, true, result);
    }
}
