package com.openjava.datalake.common.gateway;

import lombok.Data;

/**
 * @Author xjd
 * @Date 2019/9/25 15:21
 * @Version 1.0
 */
@Data
public class GatewayCredentialResp {

    private String requestId;
    private Integer code;
    private String message;
    private GatewayCredentialData data;
}
