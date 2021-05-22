package com.weirwei.cansee.mapper.dao;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 项目表
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Project对象", description="项目表")
public class Project implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增id")
    private Integer id;

    @ApiModelProperty(value = "项目id")
    private String projId;

    @ApiModelProperty(value = "项目名")
    private String projName;

    @ApiModelProperty(value = "创建者Uid")
    private String creatorUid;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    public Project(String projId, String projName, String creatorId) {
        this.projId = projId;
        this.projName = projName;
        this.creatorUid = creatorId;
        this.createTime = LocalDateTime.now();
    }
}
