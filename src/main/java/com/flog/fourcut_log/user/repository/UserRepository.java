package com.flog.fourcut_log.user.repository;

import com.flog.fourcut_log.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자 찾기
     */
    Optional<User> findByEmail(String email);

    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);

    /**
     * 닉네임으로 사용자 찾기
     */
    Optional<User> findByNickname(String nickname);

    /**
     * 닉네임 존재 여부 확인
     */
    boolean existsByNickname(String nickname);

    /**
     * 로그인 타입별 사용자 목록 조회
     */
    List<User> findByLginType(String lginType);

    /**
     * 특정 권한을 가진 사용자 목록 조회
     */
    List<User> findByRole(String role);

    // 탈퇴 시 연관 데이터 삭제 (순서 중요)
    @Modifying
    @Query(value = "DELETE FROM FLOG_TAG_BAS WHERE FLOG_ID IN (SELECT FLOG_ID FROM FLOG_BAS WHERE USER_ID = :userId)", nativeQuery = true)
    void deleteFlogTagsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM FLOG_BAS WHERE USER_ID = :userId", nativeQuery = true)
    void deleteFlogsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM TAG_BAS WHERE USER_ID = :userId", nativeQuery = true)
    void deleteTagsByUserId(@Param("userId") Long userId);
}
