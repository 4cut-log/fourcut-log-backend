package com.flog.fourcut_log.global.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
	private int status; // HTTP 상태 코드
	private T data; // 응답 데이터
	private String message; // 응답 메시지

	// 공통 응답 생성 메서드
	private static <T> ApiResponse<T> buildResponse(int status, T data, String message) {
		return ApiResponse.<T>builder()
				.status(status)
				.data(data)
				.message(message)
				.build();
	}

	// 성공 응답
	public static <T> ApiResponse<T> success(ResponseCode responseCode, T data) {
		return buildResponse(responseCode.getStatus(), data, responseCode.getMessage());
	}

	// 성공 응답 (커스텀 메시지 포함)
	public static <T> ApiResponse<T> success(ResponseCode responseCode, T data, String message) {
		return buildResponse(responseCode.getStatus(), data, message);
	}

	// 에러 응답
	public static <T> ApiResponse<T> error(ResponseCode responseCode) {
		return buildResponse(responseCode.getStatus(), null, responseCode.getMessage());
	}

	// 에러 응답 (커스텀 메시지 포함)
	public static <T> ApiResponse<T> error(ResponseCode responseCode, String message) {
		return buildResponse(responseCode.getStatus(), null,
				message != null && !message.isEmpty() ? message : responseCode.getMessage());
	}
}

