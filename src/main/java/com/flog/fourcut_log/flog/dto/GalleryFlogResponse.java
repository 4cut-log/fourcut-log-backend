package com.flog.fourcut_log.flog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GalleryFlogResponse {
    private String flogId;
    private String photoUrl;
    private String thumbnailUrl;
    private String date;
}
