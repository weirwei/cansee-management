package com.weirwei.cansee.service;

import com.weirwei.cansee.mapper.dao.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表，存放基本用户信息 服务类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
public interface IUserService extends IService<User> {

    String createUser(String telephone, String nickname);

    User getUserByTelephone(String telephone);

    User getUserByUid(String uid);
}
