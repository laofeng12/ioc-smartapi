package com.openjava.datalake.rescata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 字段包装类（ID+字段定义+字段名+资源目录ID）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ColumnInfoVo implements Serializable {
    private static final long serialVersionUID = -1883066505639258137L;

    @ApiModelProperty(value = "字段Id", required = true)
    private Long columnId;
    @ApiModelProperty(value = "字段定义（code）", required = true)
    private String columnDefinition;
    @ApiModelProperty(value = "字段名（中文）")
    private String columnName;

    @ApiModelProperty("字段类型（例如String）")
    private String columnType;

    @ApiModelProperty("是否有查阅权限")
    private Boolean viewable;
    @ApiModelProperty("是否有不脱敏权限")
    private Boolean sensitived;
    @ApiModelProperty("是否有解密权限")
    private Boolean decryption;

    @ApiModelProperty("资源目录ID")
    private Long resourceId;
}
