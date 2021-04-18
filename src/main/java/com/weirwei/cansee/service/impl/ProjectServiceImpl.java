package com.weirwei.cansee.service.impl;

import com.weirwei.cansee.mapper.dao.Project;
import com.weirwei.cansee.mapper.ProjectMapper;
import com.weirwei.cansee.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
