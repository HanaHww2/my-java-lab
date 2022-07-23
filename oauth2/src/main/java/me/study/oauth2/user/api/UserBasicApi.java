package me.study.oauth2.user.api;

import lombok.AllArgsConstructor;
import me.study.oauth2.common.dto.CommonResult;
import me.study.oauth2.user.dto.SessionUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@RestController
public class UserBasicApi {
    private HttpSession httpSession;

    @GetMapping("/api/v1/users/info")
    public CommonResult<SessionUser> getOAuthUserInfo() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        return new CommonResult<>(user);
    }
}
