package com.weirwei.cansee.controller.vo.user;

import com.weirwei.cansee.mapper.dao.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author weirwei 2021/5/19 0:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserTokenVO", description = "用户带token的信息，用于登录")
public class UserTokenVO  extends User {
    @ApiModelProperty(value = "token")
    private String token;

    public UserTokenVO(User user, String token) {
        this.setId(user.getId());
        this.setUid(user.getUid());
        this.setNick(user.getNick());
        this.setCreateTime(user.getCreateTime());
        this.setUpdateTime(user.getUpdateTime());
        this.setEmail(user.getEmail());
        this.setTelephone(user.getTelephone());
        this.setDel(user.getDel());
        this.token = token;
    }
}
