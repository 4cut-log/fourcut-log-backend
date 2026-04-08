package com.flog.fourcut_log.flog.service;

import com.flog.fourcut_log.flog.dto.TagDetailResponse;
import com.flog.fourcut_log.flog.dto.TagListResponse;
import com.flog.fourcut_log.flog.entity.Tag;
import com.flog.fourcut_log.flog.repository.FlogRepository;
import com.flog.fourcut_log.flog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final FlogRepository flogRepository;

    @Transactional(readOnly = true)
    public List<TagListResponse> getTagsWithCount(Long userId) {
        return tagRepository.findTagsWithCountByUserId(userId).stream()
                .map(row -> TagListResponse.builder()
                        .tagName((String) row[0])
                        .color((String) row[1])
                        .count(((Number) row[2]).longValue())
                        .thumbnailUrl((String) row[3])
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public TagDetailResponse getTagDetail(Long userId, String tagName) {
        Tag tag = tagRepository.findByUser_IdAndTagName(userId, tagName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그입니다."));

        List<Object[]> statsList = flogRepository.findTagStatsByUserIdAndTagName(userId, tagName);
        Object[] stats = statsList.isEmpty() ? new Object[]{0L, null, null} : statsList.get(0);
        long count = ((Number) stats[0]).longValue();
        String firstDate = stats[1] != null ? stats[1].toString() : null;
        String lastDate = stats[2] != null ? stats[2].toString() : null;

        return TagDetailResponse.builder()
                .tagName(tag.getTagName())
                .color(tag.getColor())
                .count(count)
                .firstDate(firstDate)
                .lastDate(lastDate)
                .build();
    }

    @Transactional
    public void deleteTag(Long userId, String tagName) {
        Tag tag = tagRepository.findByUser_IdAndTagName(userId, tagName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그입니다."));
        tagRepository.deleteFromJoinTable(tag.getId());
        tagRepository.delete(tag);
    }
}
