package com.openjava.datalake.external.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xjd
 * @Date 2020/4/11 20:45
 * @Version 1.0
 */
@Data
@ApiModel("通用返回对象")
public class SimpleApiResp {
    @ApiModelProperty("业务编码")
    private Integer code;
    @ApiModelProperty("业务描述")
    private String message;
}