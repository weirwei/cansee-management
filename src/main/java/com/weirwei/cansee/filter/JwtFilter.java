package com.weirwei.cansee.filter;

import com.fehead.lang.util.JWTUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        //等到请求头信息authorization信息
        final String authHeader = request.getHeader("authorization");
        String uid;
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
            return;
        }
        if (authHeader == null || !authHeader.startsWith("bearer ")) {
            request.getRequestDispatcher("/error/login").forward(request, response);
            return;
        } else {
            String token = authHeader.replace("bearer ", "");
            try {
                uid = (String) JWTUtil.parasTokenBody(token, SIGN).get("sub");
            } catch (Exception e) {
                request.setAttribute("tokenErr", e);
                // 请求转发
                request.getRequestDispatcher("/error/token").forward(request, response);
                return;
            }
            req.setAttribute("uid", uid);
            log.info("user login authorization passed, uid=" + uid);
        }

        chain.doFilter(req, res);

    }

    @Override
    public void destroy() {

    }
}
