package me.study.oauth2.config;

import lombok.RequiredArgsConstructor;
import me.study.oauth2.security.OAuth2CookieAuthorizationRequestRepository;
import me.study.oauth2.handler.JWTAccessDeniedHandler;
import me.study.oauth2.handler.JwtAuthenticationEntryPoint;
import me.study.oauth2.handler.OAuth2AuthenticationFailureHandler;
import me.study.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import me.study.oauth2.jwt.JWTAuthenticationFilter;
import me.study.oauth2.user.domain.RoleType;
import me.study.oauth2.user.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class  SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2CookieAuthorizationRequestRepository oAuth2CookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음, 토큰 방식
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**") // h2-console 페이지가 csrf 대응이 되어있지 않으므로 예외로 둔다.
                .and()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authz) -> authz
                                .antMatchers("/", "/css/**", "/images/**", "/h2-console/**").permitAll()
                                .antMatchers("/api/v1/**").hasRole(RoleType.USER.name())
                                .anyRequest().authenticated()
                )
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository(oAuth2CookieAuthorizationRequestRepository)
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)	// 401
                .accessDeniedHandler(jwtAccessDeniedHandler);		// 403

        return http.build();
    }

    /*
     * security 설정 시, 사용할 인코더 설정
     * */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
//    @Bean
//    public OAuth2CookieAuthorizationRequestRepository oAuth2AuthorizationRequestWithCookieRepository() {
//        return new OAuth2CookieAuthorizationRequestRepository();
//    }

}
