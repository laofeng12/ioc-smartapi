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
@ApiModel("API下架")
public class GatewayUnpublishReq {

    @ApiModelProperty(value = "API ID", required = true)
    private Long customApiId;
    @ApiModelProperty(value = "API类型，1查询API，9提交API", required = true, hidden = true)
    private Long moduleType;
    @ApiModelProperty(value = "API下架说明", required = false)
    private String sharedRemark;

}
