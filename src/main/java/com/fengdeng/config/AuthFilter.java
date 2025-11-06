package com.fengdeng.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean isPublic = path.startsWith("/index")
                || path.startsWith("/register")          // ★ 加上这一行
                || path.startsWith("/verify")
                || path.startsWith("/api/qrcode/trace")
                || path.startsWith("/css")
                || path.startsWith("/js");




        chain.doFilter(request, response);
    }
}
