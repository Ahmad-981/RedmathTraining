package com.practice.project06.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableConfigurationProperties(value = { ApiProperties.class})
@Configuration
@EnableMethodSecurity
public class ApiSecurityConfiguration {

    @Value("${api.security.ignored}")
    private String[] ignored;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {

            //for (String location : props.getIgnored()) {      //Using class ApiProperties
            for (String location : ignored) {                   //Using ignored array of this class
                web.ignoring().requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, location));
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(config -> config
                 .requestMatchers(HttpMethod.POST, "/api/v1/student").hasAnyAuthority("admin",
                 "student")
                 .requestMatchers(HttpMethod.PUT, "/api/v1/student/**").hasAnyAuthority("admin",
                 "teacher")
                .anyRequest()
                .authenticated());
        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                // .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
        );
        return http.build();
    }
}
