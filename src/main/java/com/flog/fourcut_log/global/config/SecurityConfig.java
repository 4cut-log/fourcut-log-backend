package com.flog.fourcut_log.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(cscf -> cscf.disable())
			.authorizeHttpRequests(authorizeRequest ->
				authorizeRequest
						.requestMatchers(
								AntPathRequestMatcher.antMatcher("/auth/**")
						).authenticated()
			)
			.formLogin(formLogin -> formLogin.disable());
		return http.build();
	}
	
}
