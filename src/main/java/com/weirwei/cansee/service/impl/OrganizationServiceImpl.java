package com.weirwei.cansee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.weirwei.cansee.controller.vo.organization.OrgPageVO;
import com.weirwei.cansee.controller.vo.organization.OrgVO;
import com.weirwei.cansee.controller.vo.role.RoleVO;
import com.weirwei.cansee.controller.vo.user.UserSinglePageVO;
import com.weirwei.cansee.controller.vo.user.UserSingleVO;
import com.weirwei.cansee.mapper.OrgProjMapper;
import com.weirwei.cansee.mapper.OrgUserMapper;
import com.weirwei.cansee.mapper.UserMapper;
import com.weirwei.cansee.mapper.dao.*;
import com.weirwei.cansee.mapper.OrganizationMapper;
import com.weirwei.cansee.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weirwei.cansee.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
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
    public OrgPageVO getOrgPage(@PageableDefault(size = 6, page = 1) Pageable pageable, String uid, String search) {

        List<OrgUser> orgUserList = orgUserMapper.selectList(new QueryWrapper<OrgUser>().eq("uid", uid));

        QueryWrapper<Organization> oqw = new QueryWrapper<>();
        // 该用户的组织和角色映射
        Map<String, Integer> orgRoleMap = new HashMap<>(pageable.getPageSize());
        // 搜索
        if (!StringUtils.isEmpty(search)) {
            oqw.eq("org_id", search).or().like("org_name", search);
        }

        List<String> selectOrgId = new ArrayList<>();
        for (OrgUser v : orgUserList) {
            selectOrgId.add(v.getOrgId());
            orgRoleMap.put(v.getOrgId(), v.getRoleId());
        }
        oqw.in("org_id", selectOrgId);

        Page<Organization> orgPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());

        // 获取组织信息
        Page<Organization> orgSelectPage = baseMapper.selectPage(orgPage, oqw);
        long pageTotal = orgSelectPage.getTotal();
        List<OrgVO> orgVOList = new ArrayList<>();

        //todo 数据查找失败时分页可能不准确，待优化
        for (Organization v : orgSelectPage.getRecords()) {
            int projNum = orgProjMapper.selectCount(new QueryWrapper<OrgProj>().eq("org_id", v.getOrgId()));
            int memberNum = orgUserMapper.selectCount(new QueryWrapper<OrgUser>().eq("org_id", v.getOrgId()));
            if (!orgRoleMap.containsKey(v.getOrgId())) {
                continue;
            }
            RoleVO roleVO = new RoleVO(orgRoleMap.get(v.getOrgId()));
            orgVOList.add(new OrgVO(v.getOrgId(), v.getOrgName(), projNum, memberNum, v.getOrgRegisterTime(), roleVO));
        }

        return new OrgPageVO(uid, orgVOList, pageTotal);
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
    public UserSinglePageVO getMemberPage(@PageableDefault(size = 6, page = 1) Pageable pageable, String orgId, String search) {
        Page<User> userPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        List<OrgUser> orgUserList = orgUserMapper.selectList(new QueryWrapper<OrgUser>().eq("org_id", orgId));
        QueryWrapper<User> uqw = new QueryWrapper<>();
        Map<String, Integer> userRoleMap = new HashMap<>(orgUserList.size());
        if (!StringUtils.isEmpty(search)) {
            uqw.eq("uid", search).or().like("nick", search);
        }

        List<String> selectUid = new ArrayList<>();
        for (OrgUser ou : orgUserList) {
            userRoleMap.put(ou.getUid(), ou.getRoleId());
            selectUid.add(ou.getUid());
        }
        uqw.in("uid", selectUid);

        Page<User> userSelectPage = userMapper.selectPage(userPage, uqw);
        List<UserSingleVO> userSingleVOList = new ArrayList<>();
        for (User user : userSelectPage.getRecords()) {
            UserSingleVO userSingleVO = new UserSingleVO(user.getUid(), user.getNick(), user.getCreateTime(),
                    new RoleVO(userRoleMap.get(user.getUid())));
            userSingleVOList.add(userSingleVO);
        }

        return new UserSinglePageVO(userSingleVOList, userSelectPage.getTotal());

    }

    @Override
    public UserSingleVO addMember(String uid, String orgId, String addUid) throws BusinessException {
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
        return new UserSingleVO(user.getUid(), user.getNick(), user.getCreateTime(), new RoleVO(Role.ORG_MEMBER));
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
        if (orgUser.getRoleId() != Role.ORG_CREATOR) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uid", appointUid));
        if (user == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        // 转移创建者角色，自降为管理员
        if (roleCode == Role.ORG_CREATOR) {
            appointCreator(uid, orgId, appointUid);
            return;
        }
        orgUserMapper.update(new OrgUser(roleCode), new QueryWrapper<OrgUser>().eq("org_id", orgId).eq("uid", appointUid));
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void appointCreator(String uid, String orgId, String appointUid) {
        orgUserMapper.update(new OrgUser(Role.ORG_CREATOR), new QueryWrapper<OrgUser>().eq("org_id", orgId).eq("uid", appointUid));
        orgUserMapper.update(new OrgUser(Role.ORG_ADMINISTRATOR), new QueryWrapper<OrgUser>().eq("org_id", orgId).eq("uid", uid));
    }
}
