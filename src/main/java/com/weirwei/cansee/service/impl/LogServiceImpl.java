package com.weirwei.cansee.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.weirwei.cansee.controller.vo.log.*;
import com.weirwei.cansee.controller.vo.organization.OrgSingleVO;
import com.weirwei.cansee.controller.vo.project.ProjSingleVO;
import com.weirwei.cansee.mapper.*;
import com.weirwei.cansee.mapper.dao.*;
import com.weirwei.cansee.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Resource
    OrgUserMapper orgUserMapper;
    @Resource
    OrgProjMapper orgProjMapper;
    @Resource
    OrganizationMapper organizationMapper;
    @Resource
    ProjectMapper projectMapper;

    @Override
    public Log addLog(Log log) {
        save(log);
        return log;
    }

    @Override
    public LogPageVO getLogPage(Pageable pageable, String orgId, String projId, String uid,
                                int code, String reqId,
                                LocalDateTime start, LocalDateTime end,
                                int solved) throws BusinessException {
        authentication(orgId, projId, uid);
        QueryWrapper<Log> lqw = new QueryWrapper<>();
        lqw.eq("solved", solved);
        if (!StringUtils.isEmpty(reqId)) {
            lqw.eq("req_id", reqId);
        }
        if (code != 0) {
            lqw.eq("log_type", code);
        }
        Page<Log> logPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        if (start != null && end != null) {
            lqw.apply("date_format (log_time,'%Y-%m-%d %H:%i:%s') >= date_format('" + start + "','%Y-%m-%d %H:%i:%s')")
                    .apply("date_format (log_time,'%Y-%m-%d %H:%i:%s') <= date_format('" + end + "','%Y-%m-%d %H:%i:%s')")
                    .orderByDesc("log_time");
        }
        // 获取组织名和项目名
        Organization organization = organizationMapper.selectOne(new QueryWrapper<Organization>().eq("org_id", orgId));
        Project project = projectMapper.selectOne(new QueryWrapper<Project>().eq("proj_id", projId));

        IPage<Log> logSelectPage = this.page(logPage, lqw);
        List<LogVO> list = new ArrayList<>();
        for (Log v : logSelectPage.getRecords()) {
            list.add(new LogVO(v.getLogId(), v.getReqId(), v.getLogType(), v.getLogMsg(), v.getLogTime(),
                    new ProjSingleVO(projId, project.getProjName()), new OrgSingleVO(orgId, organization.getOrgName()),
                    v.getLogClass(), v.getSolved()));
        }
        return new LogPageVO(orgId, projId, list, logSelectPage.getTotal());
    }

    @Override
    public void solvedLog(String orgId, String projId, String uid, String logId, int solved) throws BusinessException {
        authentication(orgId, projId, uid);

        UpdateWrapper<Log> lqw = new UpdateWrapper<>();
        update(lqw.eq("log_id", logId).set("solved", solved));
    }

    @Override
    public void delLog(String orgId, String projId, String uid, String logId) throws BusinessException {
        authentication(orgId, projId, uid);

        baseMapper.delete(new QueryWrapper<Log>().eq("log_id", logId));
    }

    @Override
    public LogNumVO getLogNum(String uid) {
        List<String> projIdList = getAllProjId(uid);

        int info = count(new QueryWrapper<Log>().eq("log_type", Log.INFO).in("proj_id", projIdList));
        int warn = count(new QueryWrapper<Log>().eq("log_type", Log.WARNING).in("proj_id", projIdList));
        int error = count(new QueryWrapper<Log>().eq("log_type", Log.ERROR).in("proj_id", projIdList));

        return new LogNumVO(info, 0, warn, error);
    }

    @Override
    public LogLineChatVO getLogLineChat(String uid) {
        List<String> projIdList = getAllProjId(uid);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[]  fiveDay = new String[7];
        LocalDateTime today = LocalDateTime.now();

        LogLineChatVO logLineChatVO = new LogLineChatVO();

        logLineChatVO.setText("近几天日志趋势图");
        List<Integer> infoData = new ArrayList<>();
        List<Integer> warnData = new ArrayList<>();
        List<Integer> errorData = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            QueryWrapper<Log> infoQW = new QueryWrapper<Log>().eq("log_type", Log.INFO).in("proj_id", projIdList);
            QueryWrapper<Log> warnQW = new QueryWrapper<Log>().eq("log_type", Log.WARNING).in("proj_id", projIdList);
            QueryWrapper<Log> errorQW = new QueryWrapper<Log>().eq("log_type", Log.ERROR).in("proj_id", projIdList);
            LocalDateTime minus = today.minus(Period.ofDays(i));
            fiveDay[i] = minus.format(dtf);
            if (i == 5) {
                continue;
            }
            labels.add(StringUtils.substring(fiveDay[i], 8) + "日");
            infoQW.apply("date_format (log_time,'%Y-%m-%d') >= date_format('" + fiveDay[i+1] + "','%Y-%m-%d')")
                    .apply("date_format (log_time,'%Y-%m-%d') < date_format('" + fiveDay[i] + "','%Y-%m-%d')");
            warnQW.apply("date_format (log_time,'%Y-%m-%d') >= date_format('" + fiveDay[i+1] + "','%Y-%m-%d')")
                    .apply("date_format (log_time,'%Y-%m-%d') < date_format('" + fiveDay[i] + "','%Y-%m-%d')");
            errorQW.apply("date_format (log_time,'%Y-%m-%d') >= date_format('" + fiveDay[i+1] + "','%Y-%m-%d')")
                    .apply("date_format (log_time,'%Y-%m-%d') < date_format('" + fiveDay[i] + "','%Y-%m-%d')");
            int info = count(infoQW);
            int warn = count(warnQW);
            int error = count(errorQW);
            infoData.add(info);
            warnData.add(warn);
            errorData.add(error);
        }
        DataSet infoSet = new DataSet("INFO", infoData);
        DataSet warnSet = new DataSet("WARN", warnData);
        DataSet errorSet = new DataSet("ERROR", errorData);
        List<DataSet> dataSets = new ArrayList<>();
        dataSets.add(infoSet);
        dataSets.add(warnSet);
        dataSets.add(errorSet);
        logLineChatVO.setLabels(labels);
        logLineChatVO.setDataSets(dataSets);

        return logLineChatVO;
    }

    /**
     * 验证参数合理性
     *
     * @param orgId  orgId
     * @param projId projId
     * @param uid    uid
     * @throws BusinessException BusinessException
     */
    private void authentication(String orgId, String projId, String uid) throws BusinessException {
        OrgProj orgProj = orgProjMapper.selectOne(new QueryWrapper<OrgProj>().eq("org_id", orgId).eq("proj_id", projId));
        if (orgProj == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "项目不存在");
        }
        OrgUser orgUser = orgUserMapper.selectOne(new QueryWrapper<OrgUser>().eq("uid", uid).eq("org_id", orgId));
        if (orgUser == null) {
            throw new BusinessException(EmBusinessError.SERVICE_REQUIRE_ROLE_ADMIN, "权限不足");
        }
    }

    /**
     * 获得用户所有项目id
     *
     * @param uid uid
     * @return List<String>
     */
    private List<String> getAllProjId(String uid) {
        List<OrgUser> orgUserList = orgUserMapper.selectList(new QueryWrapper<OrgUser>().eq("uid", uid));
        List<String> orgIdList = new ArrayList<>();
        for (OrgUser v : orgUserList) {
            orgIdList.add(v.getOrgId());
        }
        List<OrgProj> orgProjList = orgProjMapper.selectList(new QueryWrapper<OrgProj>().in("org_id", orgIdList));
        List<String> projIdList = new ArrayList<>();
        for (OrgProj v : orgProjList) {
            projIdList.add(v.getProjId());
        }
        return projIdList;
    }
}
