package com.flog.fourcut_log.flog.controller;

import com.flog.fourcut_log.flog.dto.FlogRequest;
import com.flog.fourcut_log.flog.dto.FlogResponse;
import com.flog.fourcut_log.flog.dto.FlogUpdateRequest;
import com.flog.fourcut_log.flog.dto.GalleryFlogResponse;
import com.flog.fourcut_log.flog.dto.GalleryPageResponse;
import com.flog.fourcut_log.flog.service.FlogService;
import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/flogs")
@RequiredArgsConstructor
public class FlogController {

    private final FlogService flogService;

    // @AuthenticationPrincipal: JwtAuthenticationFilter에서 SecurityContext에 저장한 email(principal)을 주입받음
    // 즉, 요청 헤더의 JWT 토큰에서 자동으로 현재 유저 email을 꺼내줌
    @PostMapping
    public ResponseEntity<ApiResponse<FlogResponse>> createFlog(@RequestBody FlogRequest request,
                                                                      @AuthenticationPrincipal Long userId) {
        FlogResponse response = flogService.createFlog(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(ResponseCode.CREATED, response, "네컷로그가 기록되었습니다."));
    }

    // @PathVariable: URL 경로의 {flogId} 값을 파라미터로 받음
    @GetMapping("/{flogId}")
    public ResponseEntity<ApiResponse<FlogResponse>> getFlog(@PathVariable Long flogId,
                                                                   @AuthenticationPrincipal Long userId) {
        FlogResponse response = flogService.getFlog(flogId, userId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response));
    }

    @PutMapping("/{flogId}")
    public ResponseEntity<ApiResponse<FlogResponse>> updateFlog(@PathVariable Long flogId,
                                                                @RequestBody FlogUpdateRequest request,
                                                                @AuthenticationPrincipal Long userId) {
        FlogResponse response = flogService.updateFlog(flogId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response, "네컷로그가 수정되었습니다."));
    }

    @DeleteMapping("/{flogId}")
    public ResponseEntity<ApiResponse<Void>> deleteFlog(@PathVariable Long flogId,
                                                        @AuthenticationPrincipal Long userId) {
        flogService.deleteFlog(flogId, userId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, null, "네컷로그가 삭제되었습니다."));
    }

    // 갤러리 최신순 목록
    @GetMapping("/gallery")
    public ResponseEntity<ApiResponse<GalleryPageResponse<GalleryFlogResponse>>> getGallery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "21") int size,
            @AuthenticationPrincipal Long userId) {
        GalleryPageResponse<GalleryFlogResponse> response = flogService.getGalleryFlogs(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response));
    }

    // 갤러리 태그별 목록
    @GetMapping("/gallery/tag")
    public ResponseEntity<ApiResponse<GalleryPageResponse<GalleryFlogResponse>>> getGalleryByTag(
            @RequestParam String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "21") int size,
            @AuthenticationPrincipal Long userId) {
        GalleryPageResponse<GalleryFlogResponse> response = flogService.getGalleryFlogsByTag(userId, tagName, page, size);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response));
    }

    // 캘린더 대표 썸네일 설정
    @PutMapping("/{flogId}/pin")
    public ResponseEntity<ApiResponse<Void>> pinFlog(@PathVariable Long flogId,
                                                     @AuthenticationPrincipal Long userId) {
        flogService.pinFlog(flogId, userId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, null));
    }
}
