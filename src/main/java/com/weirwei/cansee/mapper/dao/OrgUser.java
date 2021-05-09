package com.weirwei.cansee.mapper.dao;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户组织关联表
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrgUser对象", description="用户组织关联表")
public class OrgUser implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "组织id")
    @TableId()
    private String orgId;

    @ApiModelProperty(value = "用户id")
    @TableId()
    private String uid;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

}
