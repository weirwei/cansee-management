package com.weirwei.cansee.service;

import com.fehead.lang.error.BusinessException;
import com.weirwei.cansee.controller.vo.log.LogPageVO;
import com.weirwei.cansee.mapper.dao.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
public interface ILogService extends IService<Log> {

    Log addLog(Log log);

    LogPageVO getLogPage(Pageable pageable, String orgId, String projId, String uid,
                         int code, String reqId,
                         LocalDateTime start, LocalDateTime end) throws BusinessException;

    void solvedLog(String orgId, String projId, String uid, String logId) throws BusinessException;

    void delLog(String orgId, String projId, String uid, String logId) throws BusinessException;
}
