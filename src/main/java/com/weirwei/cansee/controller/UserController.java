package com.weirwei.cansee.controller;


import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.fehead.lang.util.CheckEmailAndTelphoneUtil;
import com.weirwei.cansee.controller.vo.user.UserSingleVO;
import com.weirwei.cansee.mapper.dao.User;
import com.weirwei.cansee.service.ILogService;
import com.weirwei.cansee.service.IPasswordService;
import com.weirwei.cansee.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.fehead.lang.controller.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表，存放基本用户信息 前端控制器
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/cansee/user")
@Slf4j
public class UserController extends BaseController {

    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;

    @Resource
    IUserService userService;
    @Resource
    ILogService logService;
    @Resource
    IPasswordService passwordService;

    @GetMapping("/info")
    public FeheadResponse getUserInfo() throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }
        User user = userService.getUserByUid(uid);
        return CommonReturnType.create(user);
    }

    @GetMapping("/logNum")
    public FeheadResponse getLogNum() throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }

        return CommonReturnType.create(logService.getLogNum(uid));
    }

    @GetMapping("/logChat")
    public FeheadResponse getLogChat() throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }

        return CommonReturnType.create(logService.getLogLineChat(uid));
    }

    @PostMapping("/register")
    public FeheadResponse register(@RequestParam String telephone,
                                   @RequestParam String nickname,
                                   @RequestParam String passwd) throws BusinessException {
        String superAdmin = "ADMIN";
        String doUid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "doUid=" + doUid +
                "&telephone=" + telephone +
                "&nickname=" + nickname +
                "&passwd=" + passwd
        );
        if (!StringUtils.equals(doUid, superAdmin)) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(passwd)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "账号或密码不能为空");
        }
        if (!CheckEmailAndTelphoneUtil.checkTelphone(telephone)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "请输入正确手机号");
        }
        String uid = passwordService.register(telephone, nickname, passwd);
        return CommonReturnType.create(uid);
    }


}

