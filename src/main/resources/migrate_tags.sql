-- =====================================================================
-- 태그 구조 마이그레이션: 1:N → N:M
-- 실행 순서: 이 SQL 먼저 실행 → 그 다음 백엔드 재시작
-- =====================================================================

-- 1단계: FLOG_TAG_BAS 중간 테이블 생성
--        (백엔드 재시작 전에 직접 생성해야 함)
CREATE TABLE IF NOT EXISTS FLOG_TAG_BAS (
    FLOG_ID BIGINT NOT NULL,
    TAG_ID  BIGINT NOT NULL,
    PRIMARY KEY (FLOG_ID, TAG_ID),
    CONSTRAINT fk_flogtag_flog FOREIGN KEY (FLOG_ID) REFERENCES FLOG_BAS(FLOG_ID) ON DELETE CASCADE,
    CONSTRAINT fk_flogtag_tag  FOREIGN KEY (TAG_ID)  REFERENCES TAG_BAS(TAG_ID)   ON DELETE CASCADE
);

-- 2단계: 기존 TAG_BAS의 FLOG_ID 연결을 FLOG_TAG_BAS로 이전
--        같은 (USER_ID, TAG_NAME)이 여러 flog에 있으면 → 가장 오래된 TAG_ID(MIN)를 대표로 사용
INSERT INTO FLOG_TAG_BAS (FLOG_ID, TAG_ID)
SELECT t.FLOG_ID, canonical.canonical_tag_id
FROM TAG_BAS t
JOIN (
    SELECT USER_ID, TAG_NAME, MIN(TAG_ID) AS canonical_tag_id
    FROM TAG_BAS
    GROUP BY USER_ID, TAG_NAME
) AS canonical
  ON canonical.USER_ID = t.USER_ID
 AND canonical.TAG_NAME = t.TAG_NAME
WHERE t.FLOG_ID IS NOT NULL
ON DUPLICATE KEY UPDATE FLOG_ID = FLOG_ID;  -- 혹시 중복 시 무시

-- 3단계: 중복 태그 제거 (USER_ID + TAG_NAME 조합 기준 최솟값 TAG_ID만 남기기)
DELETE FROM TAG_BAS
WHERE TAG_ID NOT IN (
    SELECT canonical_tag_id
    FROM (
        SELECT MIN(TAG_ID) AS canonical_tag_id
        FROM TAG_BAS
        GROUP BY USER_ID, TAG_NAME
    ) AS dedup
);

-- =====================================================================
-- 여기까지 실행 후 백엔드 재시작 (ddl-auto: update가 유니크 제약 자동 추가)
-- 재시작 후 아래 선택적 정리 구문 실행 가능
-- =====================================================================

-- [선택] FLOG_ID, UPDATED_AT 컬럼 제거 (JPA가 무시하므로 필수 아님)
-- ALTER TABLE TAG_BAS DROP FOREIGN KEY fk_tag_flog;  -- FK 이름은 실제 DB에서 확인 필요
-- ALTER TABLE TAG_BAS DROP COLUMN FLOG_ID;
-- ALTER TABLE TAG_BAS DROP COLUMN UPDATED_AT;
