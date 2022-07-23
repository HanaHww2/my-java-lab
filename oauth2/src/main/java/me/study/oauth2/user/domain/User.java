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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType role;

    @Builder
    public User(String name, String email, String picture, RoleType role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleCode() {
        return this.role.getCode();
    }

    @Getter
    @RequiredArgsConstructor
    public enum RoleType {

        ADMIN("ROLE_ADMIN", "관리자"),
        USER("ROLE_USER", "일반 사용자"),
        GUEST("ROLE_GUEST", "게스트 권한");

        private final String code;
        private final String displayName;

    }
}