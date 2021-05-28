package com.weirwei.cansee.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Log对象", description="日志表")
public class Log implements Serializable {

    private static final long serialVersionUID=1L;

    public final static int INFO = 10001;
    public final static int DEBUG = 10002;
    public final static int WARNING = 10003;
    public final static int ERROR = 10004;
    public final static int SOLVED = 1;

    @ApiModelProperty(value = "唯一自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "唯一id")
    @TableId(value = "log_id")
    private String logId;

    @ApiModelProperty(value = "请求id")
    private String reqId;

    @ApiModelProperty(value = "日志类型包括(10001, INFO)(10002, DEBUG)(10003, WARN)(10004, ERROR)")
    private Integer logType;

    @ApiModelProperty(value = "日志信息")
    private String logMsg;

    @ApiModelProperty(value = "日志产生时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime logTime;

    @ApiModelProperty(value = "日志所属的服务")
    private String projId;

    @ApiModelProperty(value = "日志在项目中所属的类")
    private String logClass;

    @ApiModelProperty(value = "日志处理状态(1已处理，0未处理)")
    private Integer solved;

    public Log(String logId, String reqId, Integer logType, String logMsg, String projId, String logClass) {
        this.logId = logId;
        this.reqId = reqId;
        this.logType = logType;
        this.logMsg = logMsg;
        this.projId = projId;
        this.logClass = logClass;
        this.logTime = LocalDateTime.now();
        this.solved = 0;
    }
}
