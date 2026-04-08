package com.flog.fourcut_log.flog.repository;

import com.flog.fourcut_log.flog.entity.Tag;
import com.flog.fourcut_log.user.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // 유저 + 태그명으로 기존 태그 조회 (find-or-create 용)
    Optional<Tag> findByUserAndTagName(User user, String tagName);

    // 유저의 태그 목록 + 색상 + flog 수 + 대표 썸네일 (최신 기록 기준)
    @Query(value = """
            SELECT t.TAG_NAME, t.COLOR, COUNT(ft.FLOG_ID) as cnt,
              (SELECT f2.PHOTO_URL FROM FLOG_BAS f2
               INNER JOIN FLOG_TAG_BAS ft2 ON f2.FLOG_ID = ft2.FLOG_ID
               WHERE ft2.TAG_ID = t.TAG_ID
               ORDER BY f2.DATE DESC LIMIT 1) as thumbnail_url
            FROM TAG_BAS t
            INNER JOIN FLOG_TAG_BAS ft ON t.TAG_ID = ft.TAG_ID
            WHERE t.USER_ID = :userId
            GROUP BY t.TAG_ID, t.TAG_NAME, t.COLOR
            ORDER BY cnt DESC
            """, nativeQuery = true)
    List<Object[]> findTagsWithCountByUserId(@Param("userId") Long userId);

    // 유저 ID + 태그명으로 태그 조회
    Optional<Tag> findByUser_IdAndTagName(Long userId, String tagName);

    // 유저의 전체 태그 수 (50개 제한 체크용)
    long countByUser(User user);

    // 태그 삭제 시 FLOG_TAG_BAS 중간 테이블에서 먼저 연결 제거
    @Modifying
    @Query(value = "DELETE FROM FLOG_TAG_BAS WHERE TAG_ID = :tagId", nativeQuery = true)
    void deleteFromJoinTable(@Param("tagId") Long tagId);
}
