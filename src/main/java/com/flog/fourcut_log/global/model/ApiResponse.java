package com.flog.fourcut_log.global.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
	private int status; // HTTP 상태 코드
	private T data; // 응답 데이터
	private String message; // 응답 메시지

}

