package com.weirwei.cansee.mapper.dao;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Organization对象", description="组织")
public class Organization implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增id")
    private Integer id;

    @ApiModelProperty(value = "组织编号")
    private String orgId;

    @ApiModelProperty(value = "组织名")
    private String orgName;

    @ApiModelProperty(value = "组织注册时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orgRegisterTime;

    public Organization(String orgId, String orgName) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgRegisterTime = LocalDateTime.now();
    }
}
