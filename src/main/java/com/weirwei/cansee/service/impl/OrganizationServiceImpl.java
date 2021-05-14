package com.weirwei.cansee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.weirwei.cansee.controller.vo.OrgListVO;
import com.weirwei.cansee.controller.vo.OrgVO;
import com.weirwei.cansee.controller.vo.RoleVO;
import com.weirwei.cansee.mapper.OrgProjMapper;
import com.weirwei.cansee.mapper.OrgUserMapper;
import com.weirwei.cansee.mapper.dao.OrgProj;
import com.weirwei.cansee.mapper.dao.OrgUser;
import com.weirwei.cansee.mapper.dao.Organization;
import com.weirwei.cansee.mapper.OrganizationMapper;
import com.weirwei.cansee.mapper.dao.Role;
import com.weirwei.cansee.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weirwei.cansee.util.IdUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    OrgUserMapper orgUserMapper;
    @Resource
    OrgProjMapper orgProjMapper;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public OrgVO createOrg(String uid, String orgName) {
        Organization organization = new Organization(IdUtil.getOrgId(), orgName);
        save(organization);
        orgUserMapper.insert(new OrgUser(organization.getOrgId(), uid, Role.ORG_CREATOR));

        return new OrgVO(organization.getOrgId(),
                organization.getOrgName(),
                0,
                1,
                organization.getOrgRegisterTime(),
                new RoleVO(Role.ORG_CREATOR));
    }

    @Override
    public OrgListVO getList(@PageableDefault(size = 6, page = 1) Pageable pageable, String uid) {

        Page<OrgUser> orgUserPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<OrgUser> ouqw = new QueryWrapper<>();
        ouqw.eq("uid", uid);
        // 分页获取该用户的组织
        Page<OrgUser> orgUserSelectPage = orgUserMapper.selectPage(orgUserPage, ouqw);
        long pageTotal = orgUserPage.getTotal();

        QueryWrapper<Organization> oqw = new QueryWrapper<>();
        // 该用户的组织和角色映射
        Map<String, Integer> orgRoleMap = new HashMap<>(pageable.getPageSize());
        for (OrgUser v : orgUserSelectPage.getRecords()) {
            oqw.eq("org_id", v.getOrgId()).or();
            orgRoleMap.put(v.getOrgId(), v.getRoleId());
        }
        // 获取组织信息
        List<Organization> organizationList = baseMapper.selectList(oqw);
        List<OrgVO> orgVOList = new ArrayList<>();
        for (Organization v : organizationList) {
            int projNum = orgProjMapper.selectCount(new QueryWrapper<OrgProj>().eq("org_id", v.getOrgId()));
            int memberNum = orgUserMapper.selectCount(new QueryWrapper<OrgUser>().eq("org_id", v.getOrgId()));
            RoleVO roleVO = new RoleVO(orgRoleMap.get(v.getOrgId()));
            orgVOList.add(new OrgVO(v.getOrgId(), v.getOrgName(), projNum, memberNum, v.getOrgRegisterTime(), roleVO));
        }

        return new OrgListVO(uid, orgVOList, pageTotal);
    }

    @Override
    public void delOrg(String uid, String orgId) throws BusinessException {

        OrgUser orgUser = orgUserMapper.selectOne(new QueryWrapper<OrgUser>().
                eq("uid", uid).
                eq("org_id", orgId));
        if (orgUser.getRoleId() != Role.ORG_CREATOR) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
        // 组织软删除
        baseMapper.delete(new QueryWrapper<Organization>().eq("org_id", orgId));
        orgUserMapper.delete(new QueryWrapper<OrgUser>().eq("org_id", orgId));
        // todo 其他相关删除
    }
}
