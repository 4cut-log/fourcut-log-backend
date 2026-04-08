package com.flog.fourcut_log.global.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.flog.fourcut_log.global.s3.dto.S3PresignedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3PresignedResponse generatePresignedUrl(String filename, String contentType) {
        // S3 저장 경로: flogs/{uuid}-{원본파일명}
        String key = "flogs/" + UUID.randomUUID() + "-" + filename;

        // presigned URL 유효시간: 10분
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withContentType(contentType)
                .withExpiration(expiration);

        URL presignedUrl = amazonS3.generatePresignedUrl(request);

        // 업로드 완료 후 실제 접근할 S3 URL
        String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        return S3PresignedResponse.builder()
                .presignedUrl(presignedUrl.toString())
                .fileUrl(fileUrl)
                .build();
    }
}
