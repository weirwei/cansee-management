package com.weirwei.cansee.controller.vo.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author weirwei 2021/5/22 10:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProjVO", description = "项目")
public class ProjVO {

    @ApiModelProperty(value = "项目id")
    private String projId;

    @ApiModelProperty(value = "项目名")
    private String projName;

    @ApiModelProperty(value = "创建者uid")
    private String creatorUid;

    @ApiModelProperty(value = "项目创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
