package me.study.userservice.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult<T> {
    private Integer code;
    private String msg;
    T data;

    public CommonResult(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }
}
