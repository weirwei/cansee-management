package com.weirwei.cansee.controller;

import com.alibaba.fastjson.JSON;
import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.weirwei.cansee.mapper.dao.Log;
import com.weirwei.cansee.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author weirwei 2021/5/23 16:24
 */
@RestController
@RequestMapping("/kafka")
@Slf4j
@CrossOrigin("*")
public class KafkaController extends BaseController {

    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;

    @Resource
    ILogService logService;

    @PostMapping("/consumer")
    public FeheadResponse consumer(@RequestParam("record") String record) throws BusinessException {
        log.info("rui:" + req.getRequestURI() +
                ",record:" + record
        );
        if (!JSON.isValid(record)) {
            log.warn("json 格式错误");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "日志数据格式非json");
        }

        Log log = JSON.parseObject(record, Log.class);
        return CommonReturnType.create(logService.addLog(log));
    }
}
