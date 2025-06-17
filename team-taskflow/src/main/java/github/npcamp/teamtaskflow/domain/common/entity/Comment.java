package github.npcamp.teamtaskflow.domain.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
//soft Delete
@SQLDelete(sql = "UPDATE comment SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //댓글 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; // 할일과 다대일 관계로 join

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // user와도 다대일 관계로 join

    @Column(nullable = false)
    private String username; // 그냥 문자열로 변경


    @Column(nullable = false)
    private String content; // 댓글에 들어갈 내용

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 삭제 여부 (soft delete 위해서)

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 삭제 날짜 (soft delete 위해서)

    // 수정
    public void updateComment(String content) {
        this.content = content;
    }
}
