package com.weirwei.cansee.controller.vo.log;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author weirwei 2021/5/29 17:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DataSet", description = "纵坐标标签及数据")
public class DataSet {
    private String label;
    private List<Integer> data;

}

