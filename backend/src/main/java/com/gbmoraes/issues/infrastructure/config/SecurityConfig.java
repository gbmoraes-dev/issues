package com.gbmoraes.issues.infrastructure.config;

import com.gbmoraes.issues.application.port.Token;
import com.gbmoraes.issues.infrastructure.security.JwtAuthFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final Token token;
  private final UserDetailsService userDetailsService;

  public SecurityConfig(Token token, UserDetailsService userDetailsService) {
    this.token = token;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(token, userDetailsService);

    return http
      .cors(c -> c.configurationSource(corsConfigurationSource()))
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(s ->
        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .exceptionHandling(e ->
        e.authenticationEntryPoint((request, response, ex) ->
          response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
        )
      )
      .authorizeHttpRequests(auth -> {
        PathPatternRequestMatcher.Builder match = PathPatternRequestMatcher.withDefaults();
        auth
          .requestMatchers(
            match.matcher("/auth/**"),
            match.matcher("/docs"),
            match.matcher("/docs/**"),
            match.matcher("/v3/api-docs/**")
          )
          .permitAll()
          .anyRequest()
          .authenticated();
      })
      .addFilterBefore(
        jwtAuthFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .build();
  }

  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(
      List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    );
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
