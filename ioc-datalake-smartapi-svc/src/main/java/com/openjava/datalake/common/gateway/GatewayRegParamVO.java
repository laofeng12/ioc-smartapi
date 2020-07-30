package com.openjava.datalake.common.gateway;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GatewayRegParamVO {

    @ApiModelProperty("参数名")
    private String paramName;
    @ApiModelProperty("参数类型")
    private String paramType;
    @ApiModelProperty("是否必传")
    private String required;
    @ApiModelProperty("参数描述")
    private String paramDesc;

}
