package com.flog.fourcut_log.flog.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CalendarResponse {

    private String date;
    private List<PhotoInfo> photos;

    @Getter
    @Builder
    public static class PhotoInfo {
        private Long id;
        private String thumbnailUrl;
        private boolean pinned;
    }
}
