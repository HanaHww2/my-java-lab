package me.study.userservice.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.study.userservice.common.BaseTimeEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // 유니크... 같은 메일이면서 다른 소셜 로그인 활용하는 경우라면?
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull // @NotEmpty 사용 불가
    private RoleType role;

    @Column(unique = true)
    private String nickname;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private AuthProviderType authProviderType;


    private Boolean activated; // 사용자 상태 - 수정 필요
    private String githubUrl;
    private String blogUrl;

    @PrePersist
    public void prePersist() {
        this.activated = this.activated == null ? true : this.activated;
    }

    @Builder
    public User(String name, String password, String email, String imageUrl, RoleType role, AuthProviderType authProviderType) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.authProviderType = authProviderType;
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