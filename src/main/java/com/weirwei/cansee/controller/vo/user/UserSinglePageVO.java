package com.weirwei.cansee.controller.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author weirwei 2021/5/19 18:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserSinglePageVO对象", description = "用户列表")
public class UserSinglePageVO {

    @ApiModelProperty(value = "用户列表")
    private List<UserSingleVO> list;

    @ApiModelProperty(value = "总页数")
    private Long pageTotal;
}
