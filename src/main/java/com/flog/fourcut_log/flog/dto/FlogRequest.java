package com.flog.fourcut_log.flog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class FlogRequest {

    // S3에 업로드된 원본 사진 URL (필수)
    private String photoUrl;

    // S3에 업로드된 크롭본 URL (필수) — 캘린더 썸네일로 사용
    private String thumbnailUrl;

    // S3에 업로드된 동영상 URL (선택)
    private String videoUrl;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String location;
    private String memoCtt;
    private List<TagRequest> tags;

    @Getter
    @NoArgsConstructor
    public static class TagRequest {
        private String tagName;
        private String color;
    }
}
