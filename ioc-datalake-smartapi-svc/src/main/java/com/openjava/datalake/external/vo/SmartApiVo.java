package com.openjava.datalake.external.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openjava.datalake.common.gateway.BaseResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @Author xjd
 * @Date 2020/4/17 12:05
 * @Version 1.0
 */
@ApiModel("智能查询API")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SmartApiVo extends BaseResp {

    @ApiModelProperty("智能查询APIID")
    private Long queryId;

    @ApiModelProperty("接口名称")
    private String queryName;

    @ApiModelProperty("接口描述")
    private String queryDesc;
    @ApiModelProperty("接口查询全地址")
    private String queryUrl;
    @ApiModelProperty("接口主机号")
    private String apiHost;
    @ApiModelProperty("接口地址")
    private String apiUrl;

    @ApiModelProperty("接口简拼")
    private String queryLogogram;
    @ApiModelProperty("接口编码")
    private String queryCode;

    @ApiModelProperty("资源目录ID")
    private Long resourceId;

    @ApiModelProperty("资源目录Code")
    private String resourceCode;

    @ApiModelProperty("资料目录名称")
    private String resourceName;


    @ApiModelProperty("每页查询数量")
    private Long pageSize;

    @ApiModelProperty("默认查询数量")
    private Long pageDefaultSize;

    @ApiModelProperty("创建人")
    private String createId;

    @ApiModelProperty("创建人账户")
    private String createUserAccount;

    @ApiModelProperty("创建人名称")
    private String createBy;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ApiModelProperty("请求接口密钥key")
    private String key;
    @ApiModelProperty("请求接口密钥secret")
    private String secret;

    @ApiModelProperty(value = "请求参数集合", required = true)
    private List<SmartApiReqParamVo> apiReqParams;

    @ApiModelProperty(value = "返回参数集合", required = true)
    private List<SmartApiRespParamVo> apiRespParams;

    public SmartApiVo(Integer code, String message) {
        super(code, message);
    }
}
