package com.example.smartenroll.common.filter;

import com.example.smartenroll.common.exception.CustomException;
import com.example.smartenroll.common.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/v1/members/login") ||
                path.startsWith("/v1/members/signup") ||
                path.startsWith("/v1/members/refresh")) {

            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        if (bearerToken != null) {
            String token = jwtUtil.resolveToken(bearerToken);

            try {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"INVALID_TOKEN\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}