package com.sparta.shop.mvc;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;


//실제 시큐리티가 동작하면 복잡하므로 가짜 시큐리티 필터를 만들어서 사용한다.
public class MockSpringSecurityFilter implements Filter { //스프링이 제공하는 기본 필터
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        //SecurityContextHolder는 인증 객체를 담는 공간
        SecurityContextHolder.getContext()
                .setAuthentication((Authentication) ((HttpServletRequest) req).getUserPrincipal());
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}