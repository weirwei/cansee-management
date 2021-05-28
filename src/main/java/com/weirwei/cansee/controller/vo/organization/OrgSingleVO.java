package com.weirwei.cansee.controller.vo.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author weirwei 2021/5/28 22:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrgSingleVO", description = "组织id和组织名")
public class OrgSingleVO {

    @ApiModelProperty(value = "组织编号")
    private String orgId;

    @ApiModelProperty(value = "组织名")
    private String orgName;
}
