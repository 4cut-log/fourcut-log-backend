package com.flog.fourcut_log.global.model;

import lombok.Getter;

@Getter
public enum ResponseCode {

	//Success
	OK(200, "Success"),
	CREATED(201, "Resource created successfully"),
	NO_CONTENT(204, "No content"),

	//Error
	BAD_REQUEST(400, "Bad request"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Not found"),
	METHOD_NOT_ALLOWED(405, "Method not allowed"),
	CONFLICT(409, "Conflict occurred"),
	TOO_MANY_REQUESTS(429, "Too many requests"),
	INTERNAL_SERVER_ERROR(500, "Internal server error"),
	SERVICE_UNAVAILABLE(503, "Service unavailable");

	private final int status;
	private final String message;

	ResponseCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
