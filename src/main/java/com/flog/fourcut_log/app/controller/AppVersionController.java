package com.flog.fourcut_log.app.controller;

import com.flog.fourcut_log.app.dto.AppVersionResponse;
import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppVersionController {

    @Value("${app.version.latest:1.0.0}")
    private String latestVersion;

    @Value("${app.version.min-required:1.0.0}")
    private String minRequiredVersion;

    @Value("${app.store.ios-url:}")
    private String iosStoreUrl;

    @Value("${app.store.aos-url:}")
    private String aosStoreUrl;

    @GetMapping("/version")
    public ResponseEntity<ApiResponse<AppVersionResponse>> getVersion() {
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.OK,
                AppVersionResponse.builder()
                        .latestVersion(latestVersion)
                        .minRequiredVersion(minRequiredVersion)
                        .iosStoreUrl(iosStoreUrl)
                        .aosStoreUrl(aosStoreUrl)
                        .build()));
    }
}
