package com.openjava.datalake.external.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author xjd
 * @Date 2020/4/17 11:29
 * @Version 1.0
 */
@ApiModel("国信平台已通过目录列表")
@Data
public class SubscribeResourceApiVo {

    @ApiModelProperty("国信平台应用id")
    private String gxAppId;
    @ApiModelProperty("国信平台应用名称")
    private String gxAppName;
    @ApiModelProperty("订阅申请表ID")
    private Long subscribeFormId;
    @ApiModelProperty(value = "流程ID",required = false)
    private Long flowId;
    @ApiModelProperty(value = "资源目录ID", required = true)
    private Long resourceId;
    @ApiModelProperty(name = "信息资源编码", required = true)
    private String resourceCode;
    @ApiModelProperty(value = "资源目录名称", required = false)
    private String resourceName;
    @ApiModelProperty(value = "资源目录版本号", required = false)
    private Long resourceVersion;

    @ApiModelProperty("申请时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime;
    @ApiModelProperty("审批时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;

    @ApiModelProperty(value = "审批人UUID", hidden = true)
    private String approverUuid;
    @ApiModelProperty(value = "审批人账号", hidden = true)
    private String approverAccount;
    @ApiModelProperty("审批人全名")
    private String approverFullname;
    @ApiModelProperty(value = "审批人所属部门ID", hidden = true)
    private String approverDeptId;
    @ApiModelProperty(value = "审批人所属部门CODE", hidden = true)
    private String approverDeptCode;
    @ApiModelProperty("审批人部门名称")
    private String approverDeptName;
    @ApiModelProperty(value = "审批人顶级部门ID", hidden = true)
    private String approverDeptTopId;
    @ApiModelProperty(value = "审批人顶级部门Code", hidden = true)
    private String approverDeptTopCode;
    @ApiModelProperty(value = "审批人顶级部门名称", hidden = true)
    private String approverDeptTopName;


    public SubscribeResourceApiVo(String gxAppId, String gxAppName, Long subscribeFormId, Long flowId,
                                  Long resourceId, String resourceCode, String resourceName, Long resourceVersion,
                                  Date applyTime, Date approvalTime) {
        this.gxAppId = gxAppId;
        this.gxAppName = gxAppName;
        this.subscribeFormId = subscribeFormId;
        this.flowId = flowId;
        this.resourceId = resourceId;
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.resourceVersion = resourceVersion;
        this.applyTime = applyTime;
        this.approvalTime = approvalTime;
    }

    public SubscribeResourceApiVo(String gxAppId, String gxAppName, Long subscribeFormId, Long flowId,
                                  Long resourceId, String resourceCode, String resourceName, Long resourceVersion,
                                  Date applyTime, Date approvalTime,
                                  String approverUuid, String approverAccount, String approverFullname,
                                  String approverDeptId, String approverDeptCode, String approverDeptName,
                                  String approverDeptTopId, String approverDeptTopCode, String approverDeptTopName) {
        this.gxAppId = gxAppId;
        this.gxAppName = gxAppName;
        this.subscribeFormId = subscribeFormId;
        this.flowId = flowId;
        this.resourceId = resourceId;
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.resourceVersion = resourceVersion;
        this.applyTime = applyTime;
        this.approvalTime = approvalTime;
        this.approverUuid = approverUuid;
        this.approverAccount = approverAccount;
        this.approverFullname = approverFullname;
        this.approverDeptId = approverDeptId;
        this.approverDeptCode = approverDeptCode;
        this.approverDeptName = approverDeptName;
        this.approverDeptTopId = approverDeptTopId;
        this.approverDeptTopCode = approverDeptTopCode;
        this.approverDeptTopName = approverDeptTopName;
    }
}
