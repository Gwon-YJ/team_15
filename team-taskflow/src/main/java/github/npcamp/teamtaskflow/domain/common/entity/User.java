package github.npcamp.teamtaskflow.domain.common.entity;

import github.npcamp.teamtaskflow.domain.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    private Boolean isDeleted = false;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String name, String userName, String password, UserRoleEnum role) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}