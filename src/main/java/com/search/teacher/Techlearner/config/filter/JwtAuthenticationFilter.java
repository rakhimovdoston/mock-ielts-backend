package com.search.teacher.Techlearner.config.filter;

import com.google.gson.Gson;
import com.search.teacher.Techlearner.config.service.CustomUserDetailsService;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.Status;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.UserRepository;
import com.search.teacher.Techlearner.service.JwtService;
import com.search.teacher.Techlearner.service.user.UserTokenService;
import com.search.teacher.Techlearner.utils.GsonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserTokenService userTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        User user = userTokenService.checkToken(jwt);
        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (user.getStatus().equals(Status.active)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().getName().name())));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User authenticated");
            } else if (user.getStatus().equals(Status.block)) {
                GsonUtils.responseError(408, "Your account is temporarily blocked", response);
                return;
            } else {
                GsonUtils.responseError(409, "Your account has not been activated", response);
                return;
            }
        }
        filterChain.doFilter(request, response);
        StringBuilder log = new StringBuilder();
        log.append("{");
        log.append("method:").append(request.getMethod()).append(", url:");
        log.append(request.getRequestURI()).append(", status_code:");
        log.append(response.getStatus()).append(", ");
        log.append("ip: ").append(request.getRemoteAddr()).append(", ");
        long duration = System.currentTimeMillis() - start;
        log.append("duration:").append(duration).append("ms");
        log.append("}");
        logger.info("ApiFilter {}", log);
    }
}
