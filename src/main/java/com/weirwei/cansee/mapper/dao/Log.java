package com.weirwei.cansee.mapper.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@ApiModel(value="Log对象", description="日志表")
public class Log implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "唯一自增id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @ApiModelProperty(value = "日志类型包括(10001, INFO)(10002, WARN)(10003, DEBUG)(10004, ERROR)")
    private String logType;

    @ApiModelProperty(value = "日志信息")
    private String logMsg;

    @ApiModelProperty(value = "日志产生时间")
    private LocalDateTime logTime;

    @ApiModelProperty(value = "日志所属的服务")
    private String projId;

    @ApiModelProperty(value = "日志在项目中所属的类")
    private String logClass;


}
