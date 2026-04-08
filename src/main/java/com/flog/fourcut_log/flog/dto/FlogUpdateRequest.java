package com.flog.fourcut_log.flog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FlogUpdateRequest {

    private String photoUrl;       // null이면 기존 유지
    private String thumbnailUrl;   // null이면 기존 유지
    private String videoUrl;
    private String date;
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
