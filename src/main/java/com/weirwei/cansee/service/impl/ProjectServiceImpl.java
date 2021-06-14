package com.weirwei.cansee.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.weirwei.cansee.controller.vo.log.LogNumVO;
import com.weirwei.cansee.controller.vo.project.ProjPageVO;
import com.weirwei.cansee.controller.vo.project.ProjVO;
import com.weirwei.cansee.mapper.LogMapper;
import com.weirwei.cansee.mapper.OrgProjMapper;
import com.weirwei.cansee.mapper.OrgUserMapper;
import com.weirwei.cansee.mapper.ProjectMapper;
import com.weirwei.cansee.mapper.dao.*;
import com.weirwei.cansee.service.IProjectService;
import com.weirwei.cansee.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    LogMapper logMapper;

    @Value("${cansee.log.df-kafkaconsumer.topic}")
    private String TOPIC;

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
        for (Project v : projSelectPage.getRecords()) {
            projVOList.add(
                    new ProjVO(v.getProjId(),
                            v.getProjName(),
                            v.getCreatorUid(),
                            logTypeCount(v.getProjId()),
                            v.getCreateTime())
            );
        }
        return new ProjPageVO(orgId, projVOList, pageTotal);
    }

    @Override
    public ProjVO addProj(String uid, String orgId, String projName) throws BusinessException {
        roleJudge(uid, orgId);
        Project project = addProjTransaction(uid, orgId, projName);

        return new ProjVO(project.getProjId(), projName, uid, new LogNumVO(), project.getCreateTime());
    }

    @Override
    public void delProj(String uid, String orgId, String projId) throws BusinessException {
        roleJudge(uid, orgId);

        delProjTransaction(orgId, projId);
    }

    @Override
    public String getProjConf(String uid, String projId) {
        Map<String, Object> confMap = new HashMap<>(1);
        Map<String, Object> logMap = new HashMap<>(2);
        Map<String, Object> consumerMap = new HashMap<>(1);
        Map<String, Object> projectMap = new HashMap<>(1);
        consumerMap.put("topic", TOPIC);
        projectMap.put("id", projId);
        logMap.put("df-kafkaconsumer", consumerMap);
        logMap.put("project", projectMap);
        confMap.put("log", logMap);

        JSONObject conf = new JSONObject(confMap);

        return conf.toJSONString();
    }

    @Transactional(rollbackFor = BusinessException.class)
    public Project addProjTransaction(String uid, String orgId, String projName) {
        Project project = new Project(IdUtil.getProjId(), projName, uid);
        save(project);
        orgProjMapper.insert(new OrgProj(orgId, project.getProjId()));
        return project;
    }

    @Transactional(rollbackFor = BusinessException.class)
    public void delProjTransaction(String orgId, String projId) {
        orgProjMapper.delete(new QueryWrapper<OrgProj>().eq("org_id", orgId).eq("proj_id", projId));
        remove(new QueryWrapper<Project>().eq("proj_id", projId));
        logMapper.delete(new QueryWrapper<Log>().eq("proj_id", projId));
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

    private LogNumVO logTypeCount(String projId) {
        List<Log> logList = logMapper.selectList(new QueryWrapper<Log>().eq("proj_id", projId));
        // 统计各类型日志数
        int info = 0, debug = 0, warning = 0, error = 0;
        for (Log log : logList) {
            switch (log.getLogType()) {
                case Log.INFO:
                    info++;
                    break;
                case Log.DEBUG:
                    debug++;
                    break;
                case Log.WARNING:
                    warning++;
                    break;
                case Log.ERROR:
                    error++;
                    break;
                default:
                    break;
            }
        }

        return new LogNumVO(info, debug, warning, error);
    }
}
