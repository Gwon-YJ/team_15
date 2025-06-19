package github.npcamp.teamtaskflow.domain.common.entity;

import github.npcamp.teamtaskflow.domain.log.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ActivityLog")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ActivityLog{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 로그 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 로그를 남긴 사용자

    @Column(nullable = false)
    private String ipAddress; // 사용자의 IP 주소

    @Column(nullable = false)
    private String requestMethod; // 요청 HTTP 메서드

    @Column(nullable = false)
    private String requestUrl; // 요청한 URL

    @Enumerated(EnumType.STRING)
    private ActivityType activityType; // 활동 유형

    @Column(nullable = false)
    private Long targetId; // 대상 ID (예: 태스크 ID, 댓글 ID 등)

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String changeContent; // 활동한 메세지
}


