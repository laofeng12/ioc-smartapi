package com.openjava.datalake.common.gateway;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("智能API对接")
@Data
public class GatewayRegApiReq {

    @ApiModelProperty("接口所属模块 1:智能API数据查询；9:数据湖数据推送 默认为1")
    private Long moduleType; //默认为智能API
    @ApiModelProperty("接口编码")
    private Long customApiId;
    @ApiModelProperty("接口名称")
    private String apiName;
    @ApiModelProperty("接口描述")
    private String apiDesc;
    @ApiModelProperty("接口地址")
    private String apiPaths;
    @ApiModelProperty("接口请求方法，默认 GET&POST")
    private String apiMethod;
    @ApiModelProperty("请求方式 默认application/json")
    private String enctype;
    @ApiModelProperty("参数数组")
    private List<GatewayRegParamVO> params;
    @ApiModelProperty("响应数组")
    private List<GatewayRegAnswerVO> answers;
    @ApiModelProperty("注册接口的用户信息（json后加密）")
    private String userInfo;
    @ApiModelProperty("是否新增")
    private Boolean isNew=true;
    @ApiModelProperty("请求事例")
    private String requestJson;
    @ApiModelProperty("返回事例")
    private String responseJson;


}
