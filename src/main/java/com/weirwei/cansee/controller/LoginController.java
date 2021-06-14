package com.weirwei.cansee.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.fehead.lang.util.CheckEmailAndTelphoneUtil;
import com.fehead.lang.util.JWTUtil;
import com.weirwei.cansee.controller.vo.user.UserTokenVO;
import com.weirwei.cansee.mapper.dao.Password;
import com.weirwei.cansee.service.IPasswordService;
import com.weirwei.cansee.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * @author weirwei 2021/4/18 17:47
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController extends BaseController {
    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;
    @Resource
    IPasswordService passwordService;
    @Resource
    IUserService userService;

    @Value("${cansee.jwt.containtime}")
    private long CONTAIN_TIME;
    @Value("${cansee.jwt.sign}")
    private String SIGN;



    @GetMapping("/register/code")
    public FeheadResponse getOtpCode(@RequestParam String telephone) {
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);
        req.getSession().setAttribute(telephone, otpCode);
        log.info(telephone + ": " + otpCode);
        //todo
        // sendSMS(telephone, otpCode);
        return CommonReturnType.create(null);
    }

    @PostMapping("/login")
    public CommonReturnType login(@RequestParam(value = "telephone") String telephone,
                                  @RequestParam(value = "passwd") String password) throws BusinessException {
        log.info("{uri:" + req.getContextPath() +
                ",param:{telephone:" + telephone +
                "}}");

        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "账号或密码不能为空");
        }
        if (!CheckEmailAndTelphoneUtil.checkTelphone(telephone)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "请输入正确手机号");
        }
        Password pass;
        try {
            pass = passwordService.getPasswordByTel(telephone);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.OPERATION_ILLEGAL, "账号不存在或密码错误");
        }
        String encodedPasswd = DigestUtil.md5Hex(password);
        if (!StringUtils.equals(encodedPasswd, pass.getPassword())) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR, "账号或密码错误");
        }
        String token;
        try {
            token = JWTUtil.generatorToken(pass.getUid(), CONTAIN_TIME, SIGN);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }
        logger.info("LOGIN: " + telephone);
        rsp.addHeader("authorization", "bearer " + token);

        return CommonReturnType.create(new UserTokenVO(userService.getUserByUid(pass.getUid()), "bearer " + token));
    }
}
