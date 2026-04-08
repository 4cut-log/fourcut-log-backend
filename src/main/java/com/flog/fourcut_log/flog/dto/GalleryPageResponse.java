package com.flog.fourcut_log.flog.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GalleryPageResponse<T> {
    private List<T> content;
    private boolean hasNext;
    private long totalCount;
}
