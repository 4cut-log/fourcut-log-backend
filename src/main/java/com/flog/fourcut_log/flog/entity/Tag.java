package com.flog.fourcut_log.flog.entity;

import com.flog.fourcut_log.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "TAG_BAS",
    uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "TAG_NAME"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;

    // 태그 소유자 — (USER_ID, TAG_NAME) 조합으로 유저당 유일한 태그
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "TAG_NAME", nullable = false, length = 50)
    private String tagName;

    @Column(name = "COLOR", nullable = false, length = 20)
    private String color;

    // 이 태그가 연결된 flog 목록 (중간 테이블: FLOG_TAG_BAS)
    @ManyToMany(mappedBy = "tags")
    private List<Flog> flogs = new ArrayList<>();

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Tag(User user, String tagName, String color) {
        this.user = user;
        this.tagName = tagName;
        this.color = color;
        this.createdAt = LocalDateTime.now();
    }
}
