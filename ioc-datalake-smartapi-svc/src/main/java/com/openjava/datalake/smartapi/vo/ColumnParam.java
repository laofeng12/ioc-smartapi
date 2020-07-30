package com.openjava.datalake.smartapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 字段包装类（ID+值）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnParam implements Serializable, Cloneable{
    private static final long serialVersionUID = -1883066505639258137L;

    @ApiModelProperty(value = "字段Id", required = true)
    private Long columnId;
    @ApiModelProperty(value = "字段Code",required = true)
    private Long columnCode;
    @ApiModelProperty("字段值")
    private String columnValue;
    @ApiModelProperty("是否模糊匹配")
    private Boolean fuzzySearch;

    @ApiModelProperty(value = "字段定义", hidden = true)
    private String columnDefinition;
    @ApiModelProperty(value = "数据类型", hidden = true)
    private Long dataType;

    @Override
    public ColumnParam clone() {
        ColumnParam clone = null;
        try {
            clone = (ColumnParam) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new AssertionError("ColumnParam.class，不同类型无法复制");
        }
        return clone;
    }
}
