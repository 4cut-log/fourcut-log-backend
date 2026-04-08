package com.flog.fourcut_log.app.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppVersionResponse {
    private String latestVersion;      // 최신 버전
    private String minRequiredVersion; // 이 버전 미만이면 강제 업데이트
    private String iosStoreUrl;
    private String aosStoreUrl;
}
