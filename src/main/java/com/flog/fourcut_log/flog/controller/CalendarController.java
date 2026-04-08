package com.flog.fourcut_log.flog.controller;

import com.flog.fourcut_log.flog.dto.CalendarResponse;
import com.flog.fourcut_log.flog.service.CalendarService;
import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    // ?yearMonth=2026-03&tagNames=붕어빵,롱길이 (tagNames 생략 시 전체 조회)
    @GetMapping
    public ResponseEntity<ApiResponse<List<CalendarResponse>>> getCalendar(
            @RequestParam String yearMonth,
            @RequestParam(required = false) List<String> tagNames,
            @AuthenticationPrincipal Long userId) {
        List<CalendarResponse> response = calendarService.getCalendar(userId, yearMonth, tagNames);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response));
    }
}
