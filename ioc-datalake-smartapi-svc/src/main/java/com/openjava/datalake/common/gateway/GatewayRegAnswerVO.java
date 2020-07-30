package com.openjava.datalake.common.gateway;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GatewayRegAnswerVO {

    @ApiModelProperty("返回字段")
    private String answerName;
    @ApiModelProperty("字段数据类型")
    private String answerType;
    @ApiModelProperty("字段含义说明")
    private String answerDesc;

}
