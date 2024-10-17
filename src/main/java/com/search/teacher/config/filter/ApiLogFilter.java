package com.search.teacher.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE)
public class ApiLogFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        long start = System.currentTimeMillis();
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
