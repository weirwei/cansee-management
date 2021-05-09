package com.weirwei.cansee.controller.vo;

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
 * @author weirwei 2021/5/4 0:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrgVO对象", description = "绑定角色的组织")
public class OrgVO {

    @ApiModelProperty(value = "组织编号")
    private String orgId;

    @ApiModelProperty(value = "组织名")
    private String orgName;

    @ApiModelProperty(value = "项目数")
    private Integer projNum;

    @ApiModelProperty(value = "成员数")
    private Integer memberNum;

    @ApiModelProperty(value = "组织注册时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orgRegisterTime;

    @ApiModelProperty(value = "角色")
    private RoleVO role;

}
