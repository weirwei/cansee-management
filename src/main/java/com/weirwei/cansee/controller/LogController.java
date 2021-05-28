package com.weirwei.cansee.controller;


import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.weirwei.cansee.mapper.dao.Log;
import com.weirwei.cansee.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.fehead.lang.controller.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/cansee/organization/{orgId}/proj/{projId}/log")
@Slf4j
public class LogController extends BaseController {

    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;

    @Resource
    ILogService logService;


    @GetMapping("/{type}")
    public FeheadResponse getLog(@PageableDefault(size = 6, page = 1) Pageable pageable,
                                 @PathVariable("orgId") String orgId,
                                 @PathVariable("projId") String projId,
                                 @PathVariable("type") String type,
                                 @RequestParam(value = "searchReqId", defaultValue = "") String searchReqId,
                                 @RequestParam(value = "start", defaultValue = "") String start,
                                 @RequestParam(value = "end", defaultValue = "") String end) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&pageable=" + pageable +
                "&orgId=" + orgId +
                "&projId=" + projId +
                "&type=" + type +
                "&searchReqId=" + searchReqId +
                "&start=" + start +
                "&end=" + end
        );
        int code = 0;
        switch (type) {
            case "info":
                code = Log.INFO;
                break;
            case "debug":
                code = Log.DEBUG;
                break;
            case "warn":
                code = Log.WARNING;
                break;
            case "error":
                code = Log.ERROR;
                break;
            case "all":
                break;
            default:
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "非法操作");
        }

        if (StringUtils.isEmpty(projId) || StringUtils.isEmpty(orgId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "非法操作");
        }
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
            startTime = LocalDateTime.ofEpochSecond(Long.parseLong(start) / 1000, 0, ZoneOffset.ofHours(8));
            endTime = LocalDateTime.ofEpochSecond(Long.parseLong(end) / 1000, 0, ZoneOffset.ofHours(8));

        }
        return CommonReturnType.create(logService.getLogPage(pageable, orgId, projId, uid, code, searchReqId, startTime, endTime));
    }

    @PutMapping("/{logId}")
    public FeheadResponse solved(@PathVariable("orgId") String orgId,
                                 @PathVariable("projId") String projId,
                                 @PathVariable("logId") String logId) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&projId=" + projId +
                "&logId=" + logId
        );

        if (StringUtils.isEmpty(projId) || StringUtils.isEmpty(orgId) || StringUtils.isEmpty(logId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "非法操作");
        }

        logService.solvedLog(orgId, projId, uid, projId);

        return CommonReturnType.create(null);
    }

    @DeleteMapping("/{logId}")
    public FeheadResponse delLog(@PathVariable("orgId") String orgId,
                                 @PathVariable("projId") String projId,
                                 @PathVariable("logId") String logId) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId +
                "&projId=" + projId +
                "&logId=" + logId
        );

        if (StringUtils.isEmpty(projId) || StringUtils.isEmpty(orgId) || StringUtils.isEmpty(logId)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "非法操作");
        }

        logService.delLog(orgId, projId, uid, projId);

        return CommonReturnType.create(null);
    }
}

