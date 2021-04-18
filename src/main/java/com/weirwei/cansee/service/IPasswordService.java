package com.weirwei.cansee.service;

import com.weirwei.cansee.mapper.dao.Password;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 存放用户密码 服务类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
public interface IPasswordService extends IService<Password> {
    Password getPasswordByTel(String telephone);

    @Transactional
    String register(String telephone, String nickname, String passwd);
}
