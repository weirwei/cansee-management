package com.weirwei.cansee.mapper.dao;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
 * 用户表，存放基本用户信息
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="用户表，存放基本用户信息")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户的唯一id")
    private String uid;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nick;

    @ApiModelProperty(value = "用户创建时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "数据更新时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "数据是否使用:0使用1删除")
    private Integer del;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    public User(String uid, String nick, String telephone) {
        this.uid = uid;
        this.nick = nick;
        this.telephone = telephone;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.del = 0;
    }
}
