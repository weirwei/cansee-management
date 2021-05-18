package com.weirwei.cansee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.weirwei.cansee.controller.vo.organization.OrgListVO;
import com.weirwei.cansee.controller.vo.organization.OrgVO;
import com.weirwei.cansee.controller.vo.role.RoleVO;
import com.weirwei.cansee.controller.vo.user.UserSingleVO;
import com.weirwei.cansee.mapper.OrgProjMapper;
import com.weirwei.cansee.mapper.OrgUserMapper;
import com.weirwei.cansee.mapper.UserMapper;
import com.weirwei.cansee.mapper.dao.*;
import com.weirwei.cansee.mapper.OrganizationMapper;
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
    @Resource
    UserMapper userMapper;

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

    @Override
    public List<UserSingleVO> getMember(@PageableDefault(size = 6, page = 1) Pageable pageable, String orgId) {
        Page<User> userPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        List<OrgUser> orgUserList = orgUserMapper.selectList(new QueryWrapper<OrgUser>().eq("org_id", orgId));
        QueryWrapper<User> uqw = new QueryWrapper<>();
        Map<String, Integer> userRoleMap = new HashMap<>(orgUserList.size());
        for (OrgUser ou : orgUserList) {
            userRoleMap.put(ou.getUid(), ou.getRoleId());
            uqw.eq("uid", ou.getUid()).or();
        }
        Page<User> userSelectPage = userMapper.selectPage(userPage, uqw);
        List<UserSingleVO> userSingleVOList = new ArrayList<>();
        for (User user : userSelectPage.getRecords()) {
            UserSingleVO userSingleVO = new UserSingleVO(user.getUid(), user.getNick(), user.getCreateTime(),
                    new RoleVO(userRoleMap.get(user.getUid())));
            userSingleVOList.add(userSingleVO);
        }

        return userSingleVOList;

    }

    @Override
    public void addMember(String uid, String orgId, String addUid) throws BusinessException {
        OrgUser orgUser = orgUserMapper.selectOne(new QueryWrapper<OrgUser>()
                .eq("uid", uid).eq("org_id", orgId));
        if (orgUser.getRoleId() != Role.ORG_CREATOR && orgUser.getRoleId() != Role.ORG_ADMINISTRATOR) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uid", addUid));
        if (user == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        orgUserMapper.insert(new OrgUser(orgId, addUid, Role.ORG_MEMBER));
    }

    @Override
    public void delMember(String uid, String orgId, String delUid) throws BusinessException {
        OrgUser orgUser = orgUserMapper.selectOne(new QueryWrapper<OrgUser>().
                eq("uid", uid).eq("org_id", orgId));
        if (orgUser.getRoleId() != Role.ORG_CREATOR && orgUser.getRoleId() != Role.ORG_ADMINISTRATOR) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uid", delUid));
        if (user == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        orgUserMapper.delete(new QueryWrapper<OrgUser>().eq("org_id", orgId).eq("uid", delUid));
    }

    @Override
    public void appointMember(String uid, String orgId, String appointUid, int roleCode) throws BusinessException {
        OrgUser orgUser = orgUserMapper.selectOne(new QueryWrapper<OrgUser>().
                eq("uid", uid).eq("org_id", orgId));
        if (orgUser.getRoleId() != Role.ORG_CREATOR && orgUser.getRoleId() != Role.ORG_ADMINISTRATOR) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uid", appointUid));
        if (user == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        orgUserMapper.update(new OrgUser(orgId, uid, roleCode), new QueryWrapper<OrgUser>().eq("org_id", orgId).eq("uid", appointUid));
    }
}
