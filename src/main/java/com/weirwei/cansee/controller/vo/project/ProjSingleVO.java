package com.weirwei.cansee.controller.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author weirwei 2021/5/28 22:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProjSingleVO", description = "项目id和项目名")
public class ProjSingleVO {

    @ApiModelProperty(value = "项目id")
    private String projId;

    @ApiModelProperty(value = "项目名")
    private String projName;
}
