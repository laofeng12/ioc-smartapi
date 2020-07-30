package com.openjava.datalake.push.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author xjd
 * @Date 2019/9/4 10:00
 * @Version 1.0
 */
@Data
@ApiModel("API同步数据接收实体")
public class ApiSyncReceiveDataVO {
    @ApiModelProperty(value = "推送唯一单号", required = true)
    private String pushSequence;
    @ApiModelProperty(value = "数据记录的开始时间戳", required = false)
    private String beginTimestamp;
    @ApiModelProperty(value = "数据记录的结束时间戳", required = false)
    private String endTImestamp;
    @ApiModelProperty(value = "字段名集合", required = true)
    private List<String> column;
    @ApiModelProperty(value = "数据集合（跟字段信息顺序一致）", required = true)
    private List<String[]> data;
    @ApiModelProperty(value = "审计信息,以json编码的字符串", required = true,hidden = false)
    private String audit_info;
    @ApiModelProperty(value = "是否全量更新（数据分批提交，默认false）", required = false, hidden = false)
    private Boolean deleteAll;
    @ApiModelProperty(value = "签名",required = true,hidden = true)
    private String sign;
    @ApiModelProperty(value = "采用的加密方式(目前支持国密SM3、SM4和HMAC-SHA256)",required = true,hidden = false)
    private String encryModel;

}
