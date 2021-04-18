package com.weirwei.cansee.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fehead.lang.error.BusinessException;
import com.weirwei.cansee.mapper.dao.Password;
import com.weirwei.cansee.mapper.PasswordMapper;
import com.weirwei.cansee.mapper.dao.User;
import com.weirwei.cansee.service.IPasswordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weirwei.cansee.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 存放用户密码 服务实现类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Service
public class PasswordServiceImpl extends ServiceImpl<PasswordMapper, Password> implements IPasswordService {

    @Resource
    IUserService userService;

    @Override
    public Password getPasswordByTel(String telephone) {
        User user = userService.getUserByTelephone(telephone);
        QueryWrapper<Password> pqw = new QueryWrapper<>();
        pqw.eq("uid", user.getUid());
        return getOne(pqw);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public String register(String telephone, String nickname, String passwd) {
        String uid = userService.createUser(telephone, nickname);

        // 单向加密
        String encodedPasswd = DigestUtil.md5Hex(passwd);
        save(new Password(uid, new String(encodedPasswd)));
        return uid;
    }
}
