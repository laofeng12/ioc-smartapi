package com.openjava.datalake.common.gateway;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xjd
 * @Date 2019/9/9 16:06
 * @Version 1.0
 */
@Data
public class GatewayUserInfoData implements Serializable {

    private String fullName;
    private String userId;
    private String account;
}
