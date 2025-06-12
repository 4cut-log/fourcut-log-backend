package com.flog.fourcut_log.global.model;

import lombok.Getter;

@Getter
public enum ResponseCode {

	//Success
	OK(200, "요청이 성공적으로 처리되었습니다."),
	CREATED(201, "리소스가 성공적으로 생성되었습니다."),
	NO_CONTENT(204, "요청이 성공적으로 처리되었으나 반환할 데이터가 없습니다."),

	//Error
	// 클라이언트 에러: 4xx
	BAD_REQUEST(400, "요청에 포함된 값이 잘못되었습니다. 입력 값을 확인해 주세요."),
	UNAUTHORIZED(401, "사용자 인증이 필요합니다. 로그인 후 다시 시도해 주세요."),
	FORBIDDEN(403, "이 작업을 수행할 권한이 없습니다. 권한을 확인해 주세요."),
	NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다. URL을 확인해 주세요."),
	METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP 메소드입니다."),
	CONFLICT(409, "데이터 충돌이 발생했습니다. 이미 존재하는 데이터입니다."),
	TOO_MANY_REQUESTS(429, "요청 횟수가 너무 많습니다. 잠시 후 다시 시도해 주세요."),

	// 서버 에러: 5xx
	INTERNAL_SERVER_ERROR(500, "서버에서 예상치 못한 문제가 발생했습니다. 관리자에게 문의해 주세요."),
	SERVICE_UNAVAILABLE(503, "서버가 일시적으로 사용 불가능합니다. 잠시 후 다시 시도해 주세요.");

	private final int status;
	private final String message;

	ResponseCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
