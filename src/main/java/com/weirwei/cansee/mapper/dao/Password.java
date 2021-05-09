package com.weirwei.cansee.mapper.dao;

import java.io.Serializable;

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
 * 存放用户密码
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
@ApiModel(value="Password对象", description="存放用户密码")
public class Password implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户唯一id")
    @TableId()
    private String uid;

    @ApiModelProperty(value = "用户密码")
    private String password;
}
