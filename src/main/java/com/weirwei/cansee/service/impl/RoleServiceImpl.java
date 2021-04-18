package com.weirwei.cansee.service.impl;

import com.weirwei.cansee.mapper.dao.Role;
import com.weirwei.cansee.mapper.RoleMapper;
import com.weirwei.cansee.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
