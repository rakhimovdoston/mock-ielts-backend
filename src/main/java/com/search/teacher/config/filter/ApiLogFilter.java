package com.search.teacher.config.filter;

import com.search.teacher.model.entities.User;
import com.search.teacher.utils.SecurityUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiLogFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SecurityUtils securityUtils;

    public ApiLogFilter(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        StringBuilder log = new StringBuilder();
        User user = securityUtils.getCurrentUser();
        log.append("{");
        log.append("method: ").append(request.getMethod()).append(", url: ");
        log.append(request.getRequestURI()).append(", status_code: ");
        log.append(response.getStatus()).append(", ");
        log.append("ip: ").append(request.getRemoteAddr()).append(", ");
        if (!request.getRequestURI().contains("/api/v1/auth"))
            log.append("user: ").append(user == null ? "anonymous" : user.getId()).append(", ");
        long duration = System.currentTimeMillis() - start;
        log.append("duration:").append(duration).append("ms").append(", ");
        log.append("}");
        logger.info("ApiFilter {}", log);
    }
}
