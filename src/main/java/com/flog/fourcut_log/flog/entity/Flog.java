package com.flog.fourcut_log.flog.entity;

import com.flog.fourcut_log.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// N:M 관계를 위한 @JoinTable은 jakarta.persistence.* 에 포함됨

@Entity
@Table(name = "FLOG_BAS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FLOG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    // 캘린더 등 목록 표시용 저해상도 썸네일 (현재는 photoUrl과 동일, 추후 Lambda로 교체)
    @Column(name = "THUMBNAIL_URL")
    private String thumbnailUrl;

    @Column(name = "VIDEO_URL")
    private String videoUrl;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "MEMO_CTT", columnDefinition = "TEXT")
    private String memoCtt;

    // 캘린더 대표 썸네일 여부 — 같은 날짜에 여러 기록이 있을 때 대표로 표시
    @Column(name = "IS_PINNED", nullable = false)
    private boolean pinned = false;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    // Flog ↔ Tag 는 N:M — 같은 태그명을 여러 flog가 공유
    // FLOG_TAG_BAS 중간 테이블로 연결
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "FLOG_TAG_BAS",
        joinColumns = @JoinColumn(name = "FLOG_ID"),
        inverseJoinColumns = @JoinColumn(name = "TAG_ID")
    )
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Flog(User user, String photoUrl, String thumbnailUrl, String videoUrl, LocalDate date, String location, String memoCtt) {
        this.user = user;
        this.photoUrl = photoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.date = date;
        this.location = location;
        this.memoCtt = memoCtt;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void pin() { this.pinned = true; }
    public void unpin() { this.pinned = false; }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void update(String photoUrl, String thumbnailUrl, String videoUrl,
                       java.time.LocalDate date, String location, String memoCtt) {
        if (photoUrl != null) this.photoUrl = photoUrl;
        if (thumbnailUrl != null) this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.date = date;
        this.location = location;
        this.memoCtt = memoCtt;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public void replaceTags(List<Tag> newTags) {
        this.tags.clear();
        this.tags.addAll(newTags);
    }
}
