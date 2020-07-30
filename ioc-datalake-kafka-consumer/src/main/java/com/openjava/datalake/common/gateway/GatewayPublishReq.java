package com.openjava.datalake.common.gateway;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author xjd
 * @Date 2020/3/30 15:08
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@ApiModel("API共享申请")
public class GatewayPublishReq {

    @ApiModelProperty(value = "API ID", required = true)
    private Long customApiId;
    @ApiModelProperty(value = "API类型，1查询API，9提交API", required = true, hidden = true)
    private Long moduleType;
    @ApiModelProperty(value = "共享的公开范围，1不公开，2公开", required = true)
    private Long openRange;
    @ApiModelProperty(value = "共享范围，1全市，2部门", required = true)
    private Long sharedScope;
    @ApiModelProperty(value = "API共享申请说明", required = false)
    private String sharedRemark;

}
