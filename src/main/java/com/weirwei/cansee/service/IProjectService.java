package com.weirwei.cansee.service;

import com.fehead.lang.error.BusinessException;
import com.weirwei.cansee.controller.vo.project.ProjPageVO;
import com.weirwei.cansee.controller.vo.project.ProjVO;
import com.weirwei.cansee.mapper.dao.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
public interface IProjectService extends IService<Project> {

    ProjPageVO getProjPage(Pageable pageable, String orgId, String search);

    ProjVO addProj(String uid, String orgId, String projName) throws BusinessException;

    void delProj(String uid, String orgId, String projId) throws BusinessException;

    String getProjConf(String uid, String projId);
}
