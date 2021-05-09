package com.weirwei.cansee.mapper.dao;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织项目关联表
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrgProj对象", description="组织项目关联表")
public class OrgProj implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "组织id")
    @TableId()
    private String orgId;

    @ApiModelProperty(value = "项目id")
    @TableId()
    private String projId;
}
