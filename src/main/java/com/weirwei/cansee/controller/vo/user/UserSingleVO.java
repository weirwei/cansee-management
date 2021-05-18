package com.weirwei.cansee.controller.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weirwei.cansee.controller.vo.role.RoleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author weirwei 2021/5/18 20:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserSingleVO", description = "用户简单信息")
public class UserSingleVO {
    @ApiModelProperty(value = "用户id")
    private String uid;
    @ApiModelProperty(value = "昵称")
    private String nick;
    @ApiModelProperty(value = "用户创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "用户角色")
    private RoleVO role;
}
