package com.weirwei.cansee.controller.vo.project;

import com.weirwei.cansee.controller.vo.organization.OrgVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author weirwei 2021/5/22 10:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProjPageVO", description = "某组织下的项目")
public class ProjPageVO {
    @ApiModelProperty(value = "组织ID")
    private String orgId;

    @ApiModelProperty(value = "项目信息")
    private List<ProjVO> list;

    @ApiModelProperty(value = "总页数")
    private Long pageTotal;
}
