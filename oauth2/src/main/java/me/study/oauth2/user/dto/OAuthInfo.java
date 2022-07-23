package me.study.oauth2.user.dto;

import lombok.Builder;
import lombok.Getter;
import me.study.oauth2.user.domain.User;

import java.util.Map;

@Getter
public class OAuthInfo {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthInfo(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthInfo of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthInfo ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(User.RoleType.USER)
                .build();
    }
}
