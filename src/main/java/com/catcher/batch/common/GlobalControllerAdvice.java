package com.catcher.batch.common;

import com.catcher.batch.common.exception.ExternalException;
import com.catcher.batch.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice({"com.catcher.batch.infrastructure", "com.catcher.batch.resource"})
public class GlobalControllerAdvice {

    @ExceptionHandler(ExternalException.class)
    public CommonResponse externalException(ExternalException ex){
        log.error(ex.getMessage(), ex);
        return new CommonResponse(ex.getCode(), false, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public CommonResponse runtimeException(RuntimeException ex){
        log.error(ex.getMessage(), ex);
        return new CommonResponse(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), false, ex.getMessage());
    }
}
