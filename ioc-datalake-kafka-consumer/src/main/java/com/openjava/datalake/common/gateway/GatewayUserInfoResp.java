package com.openjava.datalake.common.gateway;

import lombok.Data;

/**
 * @Author xjd
 * @Date 2019/9/9 16:05
 * @Version 1.0
 */
@Data
public class GatewayUserInfoResp {
    private String requestId;
    private Integer code;
    private String message;
    private GatewayUserInfoData data;
}
