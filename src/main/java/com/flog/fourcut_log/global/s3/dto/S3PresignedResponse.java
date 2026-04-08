package com.flog.fourcut_log.global.s3.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class S3PresignedResponse {

    // 프론트에서 S3에 직접 업로드할 때 사용하는 URL (10분 유효)
    private String presignedUrl;

    // 업로드 완료 후 flog 등록 시 저장할 S3 파일 URL
    private String fileUrl;
}
