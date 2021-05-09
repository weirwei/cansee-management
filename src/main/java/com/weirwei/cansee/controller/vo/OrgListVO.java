package com.weirwei.cansee.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author weirwei 2021/5/4 1:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrgListVO对象", description = "某用户的组织")
public class OrgListVO {
    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "组织信息")
    private List<OrgVO> orgVOList;

    @ApiModelProperty(value = "总页数")
    private Long pageTotal;
}
