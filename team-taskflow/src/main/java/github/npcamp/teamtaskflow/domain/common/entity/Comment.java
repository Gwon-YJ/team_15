package github.npcamp.teamtaskflow.domain.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
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
    private String content; // 댓글에 들어갈 내용

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; // 삭제 여부 (soft delete 위해서)

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 삭제 날짜 (soft delete 위해서)

    // 생성자
    public Comment(Task task, User user, String content) {
        this.task = task;
        this.user = user;
        this.content = content;
    }

    // 수정
    public void updateComment(String content) {
        this.content = content;
    }

    // 삭제(soft delete)
    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
