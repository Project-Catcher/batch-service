package com.catcher.batch.common.exception;

import com.catcher.batch.common.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private final int code;
    private final String message;

    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
