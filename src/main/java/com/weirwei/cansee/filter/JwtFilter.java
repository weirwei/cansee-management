package com.weirwei.cansee.filter;

import com.fehead.lang.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author weirwei 2021/4/18 17:54
 */
@WebFilter(urlPatterns = {"/cansee/*"}, filterName = "jwtFilter")
@Slf4j
public class JwtFilter implements Filter {
    @Value("${cansee.jwt.sign}")
    private String SIGN;

    @Override
    public void init(FilterConfig filterConfig) {

    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        //等到请求头信息authorization信息
        final String authHeader = request.getHeader("authorization");
        String uid = null;
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        }
        if (authHeader == null || !authHeader.startsWith("bearer ")) {
            request.getRequestDispatcher("/login/fail").forward(request, response);
            return;
        } else {
            String token = authHeader.replace("bearer ", "");
            uid = (String) JWTUtil.parasTokenBody(token, SIGN).get("sub");
            req.setAttribute("uid", uid);
            log.info("user login authorization passed, uid=" + uid);
        }

        chain.doFilter(req, res);

    }

    @Override
    public void destroy() {

    }
}
