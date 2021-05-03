package com.weirwei.cansee.controller.vo;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weirwei 2021/5/4 0:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RoleVO对象", description = "角色")
public class RoleVO {

    public static final Map<Integer, String> ROLE_MAP = ImmutableMap.of(
            10001, "创建者",
            10002, "管理员",
            10003, "普通成员"
    );

    @ApiModelProperty(value = "角色ID")
    private Integer code;

    @ApiModelProperty(value = "角色名")
    private String name;

    public RoleVO(Integer code) {
        this.code = code;
        this.name = ROLE_MAP.get(code);
    }
}
