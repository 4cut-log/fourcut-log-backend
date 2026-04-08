package com.flog.fourcut_log.flog.repository;

import com.flog.fourcut_log.flog.entity.Flog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FlogRepository extends JpaRepository<Flog, Long> {

    // DATE가 LocalDate(DATE 타입)이므로 between으로 해당 월의 첫날~마지막날 조회
    @Query("SELECT f FROM Flog f WHERE f.user.id = :userId AND f.date BETWEEN :start AND :end")
    List<Flog> findByUserIdAndDateBetween(@Param("userId") Long userId,
                                          @Param("start") LocalDate start,
                                          @Param("end") LocalDate end);

    // 태그 필터링: 선택된 태그명 중 하나라도 포함한 flog 조회 (DISTINCT로 중복 제거)
    @Query("SELECT DISTINCT f FROM Flog f JOIN f.tags t WHERE f.user.id = :userId AND f.date BETWEEN :start AND :end AND t.tagName IN :tagNames")
    List<Flog> findByUserIdAndDateBetweenAndTagNames(@Param("userId") Long userId,
                                                     @Param("start") LocalDate start,
                                                     @Param("end") LocalDate end,
                                                     @Param("tagNames") List<String> tagNames);

    // pin 설정 시 같은 날짜의 다른 flog 조회 (unpin 처리용)
    List<Flog> findByUser_IdAndDate(Long userId, LocalDate date);

    // 갤러리 최신순 — 유저의 모든 플로그를 날짜 내림차순으로 페이지네이션
    Page<Flog> findByUser_IdOrderByDateDesc(Long userId, Pageable pageable);

    // 갤러리 태그별 — 특정 태그가 붙은 플로그를 날짜 내림차순으로 페이지네이션
    @Query("SELECT DISTINCT f FROM Flog f JOIN f.tags t WHERE f.user.id = :userId AND t.tagName = :tagName ORDER BY f.date DESC")
    Page<Flog> findByUserIdAndTagName(@Param("userId") Long userId, @Param("tagName") String tagName, Pageable pageable);

    // 갤러리 태그별 — 전체 건수, 첫 기록일, 최근 기록일
    @Query("SELECT COUNT(f), MIN(f.date), MAX(f.date) FROM Flog f JOIN f.tags t WHERE f.user.id = :userId AND t.tagName = :tagName")
    List<Object[]> findTagStatsByUserIdAndTagName(@Param("userId") Long userId, @Param("tagName") String tagName);
}
