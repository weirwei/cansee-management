package com.weirwei.cansee.controller.vo.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author weirwei 2021/5/29 16:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LogLineChatVO", description = "折线图数据")
public class LogLineChatVO {

    @ApiModelProperty(value = "标题")
    private String text;

    @ApiModelProperty(value = "横坐标")
    private List<String> labels;

    @ApiModelProperty(value = "纵坐标")
    private List<DataSet> dataSets;

}

