package com.openjava.datalake.push.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiSyncPushDataVO {
    @ApiModelProperty(value = "版本号")
    private String version;
    @ApiModelProperty(value = "加密后传输的数据体")
    private String jsonBody;
    @ApiModelProperty(value = "签名")
    private String sign;
    @ApiModelProperty(value = "sign加密方式")
    private String signModel;
    @ApiModelProperty(value = "jsonBody加密方式")
    private String jsonBodyModel;
}
