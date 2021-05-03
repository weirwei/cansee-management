package com.weirwei.cansee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weirwei.cansee.mapper.dao.User;
import com.weirwei.cansee.mapper.UserMapper;
import com.weirwei.cansee.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weirwei.cansee.util.IdUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表，存放基本用户信息 服务实现类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public String createUser(String telephone, String nickname) {
        String uid = IdUtil.getUserId();
        User user = new User(uid, nickname, telephone);
        save(user);
        return uid;
    }

    @Override
    public User getUserByTelephone(String telephone) {
        QueryWrapper<User> uqw = new QueryWrapper<>();
        uqw.eq("telephone", telephone);
        return getOne(uqw);
    }

    @Override
    public User getUserByUid(String uid) {
        QueryWrapper<User> uqw = new QueryWrapper<>();
        uqw.eq("uid", uid);
        return getOne(uqw);
    }


}
