package com.weirwei.cansee.service.impl;

import com.weirwei.cansee.mapper.dao.Log;
import com.weirwei.cansee.mapper.LogMapper;
import com.weirwei.cansee.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
