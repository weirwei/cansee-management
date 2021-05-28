package com.weirwei.cansee.controller.vo.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weirwei.cansee.controller.vo.organization.OrgSingleVO;
import com.weirwei.cansee.controller.vo.project.ProjSingleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author weirwei 2021/5/28 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LogVO", description = "日志")
public class LogVO {

    @ApiModelProperty(value = "唯一id")
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
    private ProjSingleVO proj;

    @ApiModelProperty(value = "日志所属的组织")
    private OrgSingleVO org;

    @ApiModelProperty(value = "日志在项目中所属的类")
    private String logClass;

    @ApiModelProperty(value = "日志处理状态(1已处理，0未处理)")
    private Integer solved;

}
