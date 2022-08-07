package me.study.oauth2.user.dto;

import lombok.Builder;
import lombok.Getter;
import me.study.oauth2.user.domain.RoleType;
import me.study.oauth2.user.domain.User;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    //    private Map<String, Object> attributes;
//    private String nameAttributeKey;
//    private String name;
//    private String email;
//    private String imageUrl;
//
//    @Builder
//    public OAuth2UserInfo(Map<String, Object> attributes, String nameAttributeKey,
//                          String name, String email, String imageUrl) {
//        this.attributes = attributes;
//        this.nameAttributeKey= nameAttributeKey;
//        this.name = name;
//        this.email = email;
//        this.imageUrl = imageUrl;
//    }
//
//    public static OAuth2UserInfo of(String registrationId,
//                                    String userNameAttributeName,
//                                    Map<String, Object> attributes) {
//        return ofGoogle(userNameAttributeName, attributes);
//    }
//
//    private static OAuth2UserInfo ofGoogle(String userNameAttributeName,
//                                           Map<String, Object> attributes) {
//        return OAuth2UserInfo.builder()
//                .name((String) attributes.get("name"))
//                .email((String) attributes.get("email"))
//                .imageUrl((String) attributes.get("picture"))
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }
//
//    public User toEntity() {
//        return User.builder()
//                .name(name)
//                .email(email)
//                .imageUrl(imageUrl)
//                .role(RoleType.USER)
//                .build();
//    }
}
