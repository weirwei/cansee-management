package com.weirwei.cansee.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weirwei 2021/5/19 10:44
 */
@RestController
@RequestMapping("/error")
@Slf4j
@CrossOrigin("*")
public class ErrorController extends BaseController {
    @RequestMapping("/token")
    public void tokenErr(HttpServletRequest request) throws BusinessException {
        Exception err = (Exception) request.getAttribute("tokenErr");
        String errName = err.getClass().getSimpleName();
        if (StringUtils.equals(errName, "ExpiredJwtException")) {
            throw new BusinessException(EmBusinessError.JWT_TOKEN_EXPIRED);
        }
        throw new BusinessException(EmBusinessError.LOGIN_ERROR);
    }

    @RequestMapping("/login")
    public void loginErr(HttpServletRequest request) throws BusinessException {
        throw new BusinessException(EmBusinessError.LOGIN_ERROR);
    }
}
