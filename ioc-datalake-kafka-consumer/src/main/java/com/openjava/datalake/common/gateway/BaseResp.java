package com.openjava.datalake.common.gateway;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zmk
 * @Date 2020/01/19
 */
@Data
@NoArgsConstructor
@ApiModel("通用返回对象")
public class BaseResp {
    private String requestId;
    @ApiModelProperty("业务编码")
    private Integer code;
    @ApiModelProperty("业务描述")
    private String message;

    public BaseResp(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
