package com.flog.fourcut_log.global.s3;

import com.flog.fourcut_log.global.model.ApiResponse;
import com.flog.fourcut_log.global.model.ResponseCode;
import com.flog.fourcut_log.global.s3.dto.S3PresignedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // GET /s3/presigned-url?filename=photo.jpg&contentType=image/jpeg
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<S3PresignedResponse>> getPresignedUrl(
            @RequestParam String filename,
            @RequestParam String contentType) {
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.OK, s3Service.generatePresignedUrl(filename, contentType))
        );
    }
}
