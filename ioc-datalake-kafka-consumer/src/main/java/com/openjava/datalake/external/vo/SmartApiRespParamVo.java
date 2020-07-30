package com.openjava.datalake.external.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author xjd
 * @Date 2020/4/17 12:23
 * @Version 1.0
 */
@ApiModel("智能查询API响应参数")
@Data
@Accessors(chain = true)
public class SmartApiRespParamVo {

    @ApiModelProperty("响应参数id")
    private Long responseId;

    @ApiModelProperty("智能查询APIID")
    private Long queryId;

    @ApiModelProperty("字段id")
    private String columnId;

    @ApiModelProperty("字段Code")
    private Long columnCode;

    @ApiModelProperty("字段名")
    private String columnDefinition;

    @ApiModelProperty("字段中文名")
    private String columnName;

    @ApiModelProperty("字段类型")
    private String columnType;

    @ApiModelProperty("字段描述")
    private String columnDesc;

    @ApiModelProperty("示例值")
    private String exampleValue;

    @ApiModelProperty("默认值")
    private String defaultValue;

}
