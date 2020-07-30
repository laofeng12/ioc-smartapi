package com.openjava.datalake.push.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author xjd
 * @Date 2019/9/4 10:14
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ApiModel("api同步数据返回报文")
public class ApiSyncRespVO {

    @ApiModelProperty(value = "本次同步记录的唯一序列标识", required = true)
    private String recordSequence;
    @ApiModelProperty("请求的正确的字段名, 同步正常时不返回，由于请求时字段名不正确时返回资源目录最新的字段列表")
    private List<String> columns;
    @ApiModelProperty(value = "状态码", required = true)
    private Integer code;
    @ApiModelProperty(value = "状态信息", required = true)
    private String message;

    public ApiSyncRespVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
