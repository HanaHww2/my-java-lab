package me.study.oauth2.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.study.oauth2.common.domain.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String name;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @NotNull // @NotEmpty 사용 불가
    private RoleType role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String githubUrl;
    private String blogUrl;
    private String refreshToken;

    @Builder
    public User(String name, String email, String imageUrl, RoleType role, AuthProvider authProvider) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.authProvider = authProvider;
    }

    public User update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        return this;
    }

    public String getRoleCode() {
        return this.role.getCode();
    }

//    @Getter
//    @RequiredArgsConstructor
//    public enum RoleType {
//
//        ADMIN("ROLE_ADMIN", "관리자"),
//        USER("ROLE_USER", "일반 사용자"),
//        GUEST("ROLE_GUEST", "게스트 권한");
//
//        private final String code;
//        private final String displayName;
//    }
}