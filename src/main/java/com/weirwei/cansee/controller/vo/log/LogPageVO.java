package com.weirwei.cansee.controller.vo.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author weirwei 2021/5/28 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LogPageVO", description = "日志分页展示")
public class LogPageVO {

    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "项目ID")
    private String projId;

    @ApiModelProperty(value = "日志信息")
    private List<LogVO> list;

    @ApiModelProperty(value = "总页数")
    private Long pageTotal;
}
