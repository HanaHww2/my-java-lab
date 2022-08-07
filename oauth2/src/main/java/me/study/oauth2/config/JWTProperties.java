package me.study.oauth2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTProperties {
    @Value("${jwt.secret}")
    private String secret;

}
