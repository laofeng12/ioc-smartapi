package com.openjava.datalake.external.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xjd
 * @Date 2020/4/11 20:35
 * @Version 1.0
 */
@Data
@ApiModel("通用返回对象")
public class SimpleDataApiResp<T> extends SimpleApiResp {

    @ApiModelProperty(value = "返回内容")
    private T data;

}
