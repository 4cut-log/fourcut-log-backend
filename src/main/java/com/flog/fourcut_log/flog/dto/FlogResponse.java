package com.flog.fourcut_log.flog.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FlogResponse {

    private String flogId;
    private String userId;
    private String photoUrl;
    private String videoUrl;
    private String date;
    private String location;
    private String memoCtt;
    private List<TagResponse> tags;
    private String createdAt;

    @Getter
    @Builder
    public static class TagResponse {
        private String tagId;
        private String tagName;
        private String color;
    }
}
