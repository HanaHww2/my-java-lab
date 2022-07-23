package me.study.oauth2.user.service;

import lombok.RequiredArgsConstructor;
import me.study.oauth2.user.dto.OAuthInfo;
import me.study.oauth2.user.dto.SessionUser;
import me.study.oauth2.user.domain.User;
import me.study.oauth2.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthInfo attributes = OAuthInfo.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        // 세션이 아닌 jwt 토큰을 레디스에 저장하는 방식으로 변경할 예정!
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleCode())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    @Transactional
    public User saveOrUpdate(OAuthInfo info) {
        User user = userRepository.findByEmail(info.getEmail())
                .map(el -> el.update(info.getName(),info.getPicture()))
                .orElse(info.toEntity());

        return userRepository.save(user);
    }
}