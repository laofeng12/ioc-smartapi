package com.openjava.datalake.rescata.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 字段包装类（ID+值）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ColumnParam implements Serializable {
    private static final long serialVersionUID = -1883066505639258137L;

    @ApiModelProperty(value = "字段Id", required = true)
    private Long columnId;
    @ApiModelProperty("字段值")
    private String columnValue;

    @ApiModelProperty(value = "字段定义", hidden = true)
    private String columnDefinition;
    @ApiModelProperty(value = "数据类型", hidden = true)
    private Long dataType;
}
