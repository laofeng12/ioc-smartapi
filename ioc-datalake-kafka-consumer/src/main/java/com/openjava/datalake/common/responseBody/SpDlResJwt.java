package com.openjava.datalake.common.responseBody;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SpDlResJwt {
    @ApiModelProperty("凭证key")
    private String key;

    @ApiModelProperty("凭证密钥")
    private String secret;


    @ApiModelProperty("AES密码")
    private String aesPsw;

    @ApiModelProperty("AES偏移量")
    private String aesOffset;

    @ApiModelProperty("国密秘钥（HMAC-SHA256）、（32位16进制）")
    private String smKey;
}
