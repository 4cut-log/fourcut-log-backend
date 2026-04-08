package com.flog.fourcut_log.flog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagDetailResponse {
    private String tagName;
    private String color;
    private long count;
    private String firstDate;
    private String lastDate;
}
