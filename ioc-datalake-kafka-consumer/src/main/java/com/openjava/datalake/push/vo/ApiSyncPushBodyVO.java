package com.openjava.datalake.push.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiSyncPushBodyVO {
    @ApiModelProperty(value = "资源目录编码")
    private String resourceCode;
    @ApiModelProperty(value = "推送唯一单号")
    private String pushSequence;
    @ApiModelProperty(value = "数据记录的开始时间戳")
    private String beginTimestamp;
    @ApiModelProperty(value = "数据记录的结束时间戳")
    private String endTImestamp;
    @ApiModelProperty(value = "字段名集合")
    private List<String> column;
    @ApiModelProperty(value = "数据集合（跟字段信息顺序一致）")
    private List<String[]> data;
}
