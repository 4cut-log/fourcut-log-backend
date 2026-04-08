package com.flog.fourcut_log.flog.controller;

import com.flog.fourcut_log.flog.dto.TagDetailResponse;
import com.flog.fourcut_log.flog.dto.TagListResponse;
import com.flog.fourcut_log.flog.service.TagService;
import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // GET /tags — 유저의 태그 목록 + 사용 횟수 + 대표 썸네일
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagListResponse>>> getTags(
            @AuthenticationPrincipal Long userId) {
        List<TagListResponse> response = tagService.getTagsWithCount(userId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response));
    }

    // GET /tags/{tagName} — 태그 상세 (건수, 첫 기록일, 최근 기록일)
    @GetMapping("/{tagName}")
    public ResponseEntity<ApiResponse<TagDetailResponse>> getTagDetail(
            @PathVariable String tagName,
            @AuthenticationPrincipal Long userId) {
        TagDetailResponse response = tagService.getTagDetail(userId, tagName);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, response));
    }

    // DELETE /tags/{tagName} — 태그 삭제 (연결된 플로그에서도 제거)
    @DeleteMapping("/{tagName}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(
            @PathVariable String tagName,
            @AuthenticationPrincipal Long userId) {
        tagService.deleteTag(userId, tagName);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK, null, "태그가 삭제되었습니다."));
    }
}
