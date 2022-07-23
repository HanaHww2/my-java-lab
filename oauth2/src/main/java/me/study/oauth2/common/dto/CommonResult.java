package me.study.oauth2.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommonResult<T> {
    T data;
    private int code;
    private String msg;

    public CommonResult(T data) {
        this.data = data;
    }
}
