package me.study.userservice.user.domain;

import lombok.Getter;

@Getter
public enum AuthProviderType {
    LOCAL, GOOGLE, NAVER, KAKAO, GITHUB
}