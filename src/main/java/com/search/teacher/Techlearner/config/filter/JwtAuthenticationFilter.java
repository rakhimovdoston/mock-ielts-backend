package com.search.teacher.Techlearner.config.filter;

import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.Status;
import com.search.teacher.Techlearner.service.user.UserTokenService;
import com.search.teacher.Techlearner.utils.GsonUtils;
import com.search.teacher.Techlearner.utils.ResponseMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserTokenService userTokenService;

    public JwtAuthenticationFilter(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
            } else if (user.getStatus().equals(Status.block)) {
                GsonUtils.responseError(409, ResponseMessage.USER_BLOCKED, response);
                return;
            } else {
                GsonUtils.responseError(410, ResponseMessage.USER_NOT_ACTIVATED, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
