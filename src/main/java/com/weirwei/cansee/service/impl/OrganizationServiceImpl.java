package com.weirwei.cansee.service.impl;

import com.weirwei.cansee.mapper.dao.Organization;
import com.weirwei.cansee.mapper.OrganizationMapper;
import com.weirwei.cansee.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 组织 服务实现类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

}
