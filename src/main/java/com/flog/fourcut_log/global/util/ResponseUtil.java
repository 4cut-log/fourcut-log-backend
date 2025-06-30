package com.flog.fourcut_log.global.util;

import org.springframework.http.ResponseEntity;

import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;

public class ResponseUtil {

    private ResponseUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    // 성공 응답
    public static <T> ResponseEntity<ApiResponse<T>> success(ResponseCode code, T data) {
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.<T>builder()
                        .status(code.getStatus())
                        .message(code.getMessage())
                        .data(data)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(ResponseCode.OK, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success(ResponseCode.CREATED, data);
    }

    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return ResponseEntity.status(ResponseCode.NO_CONTENT.getStatus())
                .body(ApiResponse.<Void>builder()
                        .status(ResponseCode.NO_CONTENT.getStatus())
                        .message(ResponseCode.NO_CONTENT.getMessage())
                        .data(null)
                        .build());
    }

    // 에러 응답 (기본 메시지)
    public static ResponseEntity<ApiResponse<Void>> error(ResponseCode code) {
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.<Void>builder()
                        .status(code.getStatus())
                        .message(code.getMessage())
                        .data(null)
                        .build());
    }

    // 에러 응답 (커스텀 메시지)
    public static ResponseEntity<ApiResponse<Void>> error(ResponseCode code, String customMessage) {
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.<Void>builder()
                        .status(code.getStatus())
                        .message(customMessage)
                        .data(null)
                        .build());
    }
}

