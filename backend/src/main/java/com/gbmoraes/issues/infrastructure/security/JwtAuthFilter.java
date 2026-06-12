package com.gbmoraes.issues.infrastructure.security;

import com.gbmoraes.issues.application.port.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

  private final Token token;
  private final UserDetailsService userDetailsService;

  public JwtAuthFilter(Token token, UserDetailsService userDetailsService) {
    this.token = token;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String rawToken = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      rawToken = authHeader.substring(7);
    }

    if (rawToken != null && token.validate(rawToken)) {
      String id = token.decode(rawToken);
      UserDetails userDetails = userDetailsService.loadUserByUsername(id);
      UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    chain.doFilter(request, response);
  }
}
