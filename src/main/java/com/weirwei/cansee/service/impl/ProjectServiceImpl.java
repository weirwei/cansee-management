package com.weirwei.cansee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.weirwei.cansee.controller.vo.project.ProjPageVO;
import com.weirwei.cansee.controller.vo.project.ProjVO;
import com.weirwei.cansee.mapper.OrgProjMapper;
import com.weirwei.cansee.mapper.OrgUserMapper;
import com.weirwei.cansee.mapper.ProjectMapper;
import com.weirwei.cansee.mapper.dao.OrgProj;
import com.weirwei.cansee.mapper.dao.OrgUser;
import com.weirwei.cansee.mapper.dao.Project;
import com.weirwei.cansee.mapper.dao.Role;
import com.weirwei.cansee.service.IProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    OrgProjMapper orgProjMapper;
    @Resource
    OrgUserMapper orgUserMapper;

    @Override
    public ProjPageVO getProjPage(Pageable pageable, String orgId, String search) {
        List<OrgProj> orgProjList = orgProjMapper.selectList(new QueryWrapper<OrgProj>().eq("org_id", orgId));

        List<String> selectProjId = new ArrayList<>();
        for (OrgProj v : orgProjList) {
            selectProjId.add(v.getProjId());
        }
        QueryWrapper<Project> pqw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(search)) {
            pqw.eq("proj_id", search).or().like("proj_name", search);
        }
        pqw.in("proj_id", selectProjId);

        Page<Project> projPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        Page<Project> projSelectPage = baseMapper.selectPage(projPage, pqw);
        long pageTotal = projSelectPage.getTotal();
        List<ProjVO> projVOList = new ArrayList<>();
        for (Project v: projSelectPage.getRecords()) {
            projVOList.add(new ProjVO(v.getProjId(), v.getProjName(), v.getCreatorId(), v.getCreateTime()));
        }
        return new ProjPageVO(orgId, projVOList, pageTotal);
    }

    @Override
    public ProjVO addProj(String uid, String orgId, String projName) throws BusinessException {
        roleJudge(uid, orgId);

        return addProjTransaction(uid, orgId, projName);
    }

    @Override
    public void delProj(String uid, String orgId, String projId) throws BusinessException {
        roleJudge(uid, orgId);

        delProjTransaction(orgId, projId);
    }

    @Override
    public void getProjConf(String uid, String orgId, String projId) {
        //todo 获得项目配置
    }

    @Transactional(rollbackFor = BusinessException.class)
    public ProjVO addProjTransaction(String uid, String orgId, String projName) {
        Project project = new Project(orgId, projName, uid);
        save(project);
        orgProjMapper.insert(new OrgProj(orgId, project.getProjId()));
        return new ProjVO(orgId, projName, uid, project.getCreateTime());
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void delProjTransaction(String orgId, String projId) {
        orgProjMapper.delete(new QueryWrapper<OrgProj>().eq("org_id", orgId).eq("proj_id", projId));
        remove(new QueryWrapper<Project>().eq("proj_id", projId));
    }

    private void roleJudge(String uid, String orgId) throws BusinessException {
        OrgUser orgUser = orgUserMapper.selectOne(new QueryWrapper<OrgUser>().eq("uid", uid).eq("org_id", orgId));
        if (orgUser == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户组织关系不存在");
        }
        if (orgUser.getRoleId() != Role.ORG_ADMINISTRATOR && orgUser.getRoleId() != Role.ORG_CREATOR) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
    }
}
