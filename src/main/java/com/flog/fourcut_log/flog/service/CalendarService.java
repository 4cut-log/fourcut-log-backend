package com.flog.fourcut_log.flog.service;

import com.flog.fourcut_log.flog.dto.CalendarResponse;
import com.flog.fourcut_log.flog.entity.Flog;
import com.flog.fourcut_log.flog.repository.FlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final FlogRepository flogRepository;

    @Transactional(readOnly = true)
    public List<CalendarResponse> getCalendar(Long userId, String yearMonth, List<String> tagNames) {
        LocalDate start = LocalDate.parse(yearMonth + "-01");
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // tagNames가 있으면 태그 필터링, 없으면 전체 조회
        List<Flog> flogs = (tagNames != null && !tagNames.isEmpty())
                ? flogRepository.findByUserIdAndDateBetweenAndTagNames(userId, start, end, tagNames)
                : flogRepository.findByUserIdAndDateBetween(userId, start, end);

        // 날짜별로 그룹핑하여 CalendarResponse 리스트 생성
        // Map<날짜, 해당 날짜의 flog 리스트> 형태로 그룹핑
        // LocalDate → String으로 변환하여 그룹핑
        return flogs.stream()
                .collect(java.util.stream.Collectors.groupingBy(flog -> flog.getDate().toString()))
                .entrySet().stream()
                .map(entry -> CalendarResponse.builder()
                        .date(entry.getKey())
                        .photos(entry.getValue().stream()
                                .sorted((a, b) -> {
                                    // pinned된 항목 우선, 그 다음 최신순
                                    if (a.isPinned() != b.isPinned()) return a.isPinned() ? -1 : 1;
                                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                                })
                                .map(flog -> CalendarResponse.PhotoInfo.builder()
                                        .id(flog.getId())
                                        .thumbnailUrl(flog.getThumbnailUrl())
                                        .pinned(flog.isPinned())
                                        .build())
                                .toList())
                        .build())
                .sorted(java.util.Comparator.comparing(CalendarResponse::getDate))
                .toList();
    }
}
