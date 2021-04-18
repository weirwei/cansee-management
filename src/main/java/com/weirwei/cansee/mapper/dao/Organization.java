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
 * 组织
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Organization对象", description="组织")
public class Organization implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "组织编号")
    private String orgId;

    @ApiModelProperty(value = "组织名")
    private String orgName;

    @ApiModelProperty(value = "组织注册时间")
    private LocalDateTime orgRegisterTime;

    @ApiModelProperty(value = "组织状态(1, 正常)(0, 已注销)")
    private Integer orgStatus;


}
