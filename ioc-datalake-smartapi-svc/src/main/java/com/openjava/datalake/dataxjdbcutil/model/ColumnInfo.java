package com.openjava.datalake.dataxjdbcutil.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xjd
 * @Date 2019/9/2 17:54
 * @Version 1.0
 */
@Data
@ApiModel("字段属性信息")
public class ColumnInfo {
    @ApiModelProperty("资源信息项ID")
    private Long structureId;
    @ApiModelProperty("字段sqlType")
    private int columnSqltype;
    @ApiModelProperty("字段名")
    private String columnName;
    @ApiModelProperty("字段注释")
    private String columnComment;
    @ApiModelProperty("数据类型（dl.column.datatype）（1短字符、2较长字符、3长字符、4日期型、5整数型、6小数型）")
    private Long dataType;
    @ApiModelProperty("字段长度")
    private Long columnLength;
    @ApiModelProperty("日期格式")
    private String dataFormat;
    @ApiModelProperty("是否主键（1是、0否）")
    private Long isPrimaryKey;
    @ApiModelProperty("小数位数（精度）")
    private Long decimalLength;
    @ApiModelProperty("是否可为空（1可以为空、0不可为空）")
    private Long isNullable;

}
