package com.openjava.datalake.smartapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author zjf
 * @Description 请求API网关返回的http Response类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseVO implements Serializable {
    private String requestId;//随机码
    private String code;//状态 200 正常
    private String message;//消息
}
