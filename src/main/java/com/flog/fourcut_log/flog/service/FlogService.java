package com.flog.fourcut_log.flog.service;

import com.flog.fourcut_log.flog.dto.FlogRequest;
import com.flog.fourcut_log.flog.dto.FlogResponse;
import com.flog.fourcut_log.flog.dto.FlogUpdateRequest;
import com.flog.fourcut_log.flog.dto.GalleryFlogResponse;
import com.flog.fourcut_log.flog.dto.GalleryPageResponse;
import com.flog.fourcut_log.flog.entity.Flog;
import com.flog.fourcut_log.flog.entity.Tag;
import com.flog.fourcut_log.flog.repository.FlogRepository;
import com.flog.fourcut_log.flog.repository.TagRepository;
import com.flog.fourcut_log.user.entity.User;
import com.flog.fourcut_log.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlogService {

    private final FlogRepository flogRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional
    public FlogResponse createFlog(Long userId, FlogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Flog flog = Flog.builder()
                        .user(user)
                        .photoUrl(request.getPhotoUrl())
                        .thumbnailUrl(request.getThumbnailUrl())
                        .videoUrl(request.getVideoUrl())
                        .date(request.getDate())
                        .location(request.getLocation())
                        .memoCtt(request.getMemoCtt())
                        .build();

        // 태그: 같은 이름이 이미 있으면 기존 태그 재사용, 없으면 신규 생성
        if (request.getTags() != null) {
            for (FlogRequest.TagRequest tagRequest : request.getTags()) {
                Tag tag = findOrCreateTag(user, tagRequest.getTagName(), tagRequest.getColor());
                flog.addTag(tag);
            }
        }

        Flog saved = flogRepository.save(flog);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public FlogResponse getFlog(Long flogId, Long userId) {
        Flog flog = flogRepository.findById(flogId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다."));

        if (!flog.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        return toResponse(flog);
    }

    @Transactional
    public void pinFlog(Long flogId, Long userId) {
        Flog flog = flogRepository.findById(flogId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다."));

        if (!flog.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        // 같은 날짜의 모든 flog unpin 후 선택한 flog만 pin
        flogRepository.findByUser_IdAndDate(userId, flog.getDate())
                .forEach(Flog::unpin);
        flog.pin();
    }

    @Transactional
    public FlogResponse updateFlog(Long flogId, Long userId, FlogUpdateRequest request) {
        Flog flog = flogRepository.findById(flogId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다."));

        if (!flog.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        flog.update(
                request.getPhotoUrl(),
                request.getThumbnailUrl(),
                request.getVideoUrl(),
                java.time.LocalDate.parse(request.getDate()),
                request.getLocation(),
                request.getMemoCtt()
        );

        // 태그 교체: 기존 연결 끊고 새 태그(find-or-create)로 교체
        List<Tag> newTags = new ArrayList<>();
        if (request.getTags() != null) {
            for (FlogUpdateRequest.TagRequest tagRequest : request.getTags()) {
                newTags.add(findOrCreateTag(flog.getUser(), tagRequest.getTagName(), tagRequest.getColor()));
            }
        }
        flog.replaceTags(newTags);

        return toResponse(flog);
    }

    @Transactional
    public void deleteFlog(Long flogId, Long userId) {
        Flog flog = flogRepository.findById(flogId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다."));

        if (!flog.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        flogRepository.delete(flog);
    }

    // 갤러리 최신순
    @Transactional(readOnly = true)
    public GalleryPageResponse<GalleryFlogResponse> getGalleryFlogs(Long userId, int page, int size) {
        Page<Flog> result = flogRepository.findByUser_IdOrderByDateDesc(userId, PageRequest.of(page, size));
        List<GalleryFlogResponse> content = result.getContent().stream()
                .map(f -> GalleryFlogResponse.builder()
                        .flogId(String.valueOf(f.getId()))
                        .photoUrl(f.getPhotoUrl())
                        .thumbnailUrl(f.getThumbnailUrl())
                        .date(f.getDate().toString())
                        .build())
                .toList();
        return GalleryPageResponse.<GalleryFlogResponse>builder()
                .content(content)
                .hasNext(!result.isLast())
                .totalCount(result.getTotalElements())
                .build();
    }

    // 갤러리 태그별
    @Transactional(readOnly = true)
    public GalleryPageResponse<GalleryFlogResponse> getGalleryFlogsByTag(Long userId, String tagName, int page, int size) {
        Page<Flog> result = flogRepository.findByUserIdAndTagName(userId, tagName, PageRequest.of(page, size));
        List<GalleryFlogResponse> content = result.getContent().stream()
                .map(f -> GalleryFlogResponse.builder()
                        .flogId(String.valueOf(f.getId()))
                        .photoUrl(f.getPhotoUrl())
                        .thumbnailUrl(f.getThumbnailUrl())
                        .date(f.getDate().toString())
                        .build())
                .toList();
        return GalleryPageResponse.<GalleryFlogResponse>builder()
                .content(content)
                .hasNext(!result.isLast())
                .totalCount(result.getTotalElements())
                .build();
    }

    // 유저 + 태그명으로 기존 태그 조회, 없으면 새로 생성 (최대 50개 제한)
    private Tag findOrCreateTag(User user, String tagName, String color) {
        return tagRepository.findByUserAndTagName(user, tagName)
                .orElseGet(() -> {
                    if (tagRepository.countByUser(user) >= 50) {
                        throw new IllegalStateException("태그는 최대 50개까지 등록할 수 있습니다.");
                    }
                    return tagRepository.save(Tag.builder()
                            .user(user)
                            .tagName(tagName)
                            .color(color)
                            .build());
                });
    }

    private FlogResponse toResponse(Flog flog) {
        List<FlogResponse.TagResponse> tagResponses = flog.getTags().stream()
                .map(tag -> FlogResponse.TagResponse.builder()
                        .tagId(String.valueOf(tag.getId()))
                        .tagName(tag.getTagName())
                        .color(tag.getColor())
                        .build())
                .toList();

        return FlogResponse.builder()
                .flogId(String.valueOf(flog.getId()))
                .userId(String.valueOf(flog.getUser().getId()))
                .photoUrl(flog.getPhotoUrl())
                .videoUrl(flog.getVideoUrl())
                .date(flog.getDate().toString())
                .location(flog.getLocation())
                .memoCtt(flog.getMemoCtt())
                .tags(tagResponses)
                .createdAt(flog.getCreatedAt().toString())
                .build();
    }
}
