package com.weirwei.cansee.mapper.dao;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Project对象", description="项目表")
public class Project implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "项目id")
    private String projId;

    @ApiModelProperty(value = "项目名")
    private String projName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态(0, 不存在)(1, 存在)")
    private Integer status;


}
