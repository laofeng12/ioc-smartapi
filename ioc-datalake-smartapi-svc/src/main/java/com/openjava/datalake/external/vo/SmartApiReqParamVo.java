package com.openjava.datalake.external.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xjd
 * @Date 2020/4/17 12:23
 * @Version 1.0
 */
@ApiModel("智能查询API请求参数")
@Data
public class SmartApiReqParamVo {

    @ApiModelProperty("请求参数ID")
    private Long requestId;

    @ApiModelProperty("智能查询APIID")
    private Long queryId;

    @ApiModelProperty("字段ID")
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

    @ApiModelProperty("是否必填：0非必填、1必填")
    private Long requiredMark;

    @ApiModelProperty("是否模糊搜索：0精确搜索、1模糊搜索")
    private Long fuzzyMark;
}
