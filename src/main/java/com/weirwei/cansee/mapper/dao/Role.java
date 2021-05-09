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
 * 角色表
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Role对象", description = "角色表")
public class Role implements Serializable {

    public static final int ORG_CREATOR = 10001;
    public static final int ORG_ADMINISTRATOR = 10002;
    public static final int ORG_MEMBER = 10003;
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id(10001, leader)(10002, manager)(10003, member)")
    @TableId()
    private Integer roleId;

    @ApiModelProperty(value = "权限字符串")
    private String privilege;


}
