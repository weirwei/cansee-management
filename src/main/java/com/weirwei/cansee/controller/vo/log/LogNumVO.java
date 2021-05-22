package com.weirwei.cansee.controller.vo.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author weirwei 2021/5/22 17:25
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProjPageVO", description = "各类型日志数")
public class LogNumVO {

    @ApiModelProperty(value = "info类信息")
    private Integer info;

    @ApiModelProperty(value = "debug类信息")
    private Integer debug;

    @ApiModelProperty(value = "warning类信息")
    private Integer warning;

    @ApiModelProperty(value = "error类信息")
    private Integer error;

    public LogNumVO() {
        this.info = 0;
        this.debug = 0;
        this.warning = 0;
        this.error = 0;
    }
}
