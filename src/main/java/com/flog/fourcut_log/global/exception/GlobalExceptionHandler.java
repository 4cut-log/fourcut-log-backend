package com.flog.fourcut_log.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import com.flog.fourcut_log.global.util.ResponseUtil;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        return ResponseUtil.error(e.getResponseCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnknownException(Exception e) {
        log.error("[UNEXPECTED ERROR]", e);
        return ResponseUtil.error(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
