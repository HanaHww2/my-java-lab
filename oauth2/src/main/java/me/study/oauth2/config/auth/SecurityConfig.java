package me.study.oauth2.config.auth;

import lombok.AllArgsConstructor;
import me.study.oauth2.user.domain.User.RoleType;
import me.study.oauth2.user.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests((authz) -> authz
                                .antMatchers("/", "/css/**", "/images/**", "/h2-console/**").permitAll()
                                .antMatchers("/api/v1/**").hasRole(RoleType.USER.name())
                                .anyRequest().authenticated()
                )
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
                //.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /*
     * security 설정 시, 사용할 인코더 설정
     * */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
