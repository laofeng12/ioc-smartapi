package com.openjava.datalake.external.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author xjd
 * @Date 2020/4/13 19:42
 * @Version 1.0
 */
@ApiModel("订阅工单信息")
@Data
public class SubscribeTaskOrderVo {

    @ApiModelProperty("订阅申请表ID")
    private Long subscribeFormId;
    @ApiModelProperty("流程ID")
    private Long flowId;
    @ApiModelProperty("任务ID")
    private Long taskId;
    @ApiModelProperty(value = "资源目录ID", required = true)
    private Long resourceId;
    @ApiModelProperty(name = "信息资源编码", required = true)
    private String resourceCode;

    @ApiModelProperty(value = "系统工单号", required = true)
    @Column(name = "WORK_NUM")
    private String workNum;

    @ApiModelProperty(value = "资源目录所属局ID", required = false)
    private String resourceDeptId;
    @ApiModelProperty(value = "资源目录所属局名称", required = false)
    private String resourceDeptName;
    @ApiModelProperty(value = "资源目录数据提供负责人账号", required = true)
    private String resBsOwnerAccount;
    @ApiModelProperty(value = "资源目录库表标识", required = false)
    private String resourceTableName;
    @ApiModelProperty(value = "资源目录名称", required = false)
    private String resourceName;
    @ApiModelProperty(value = "资源目录版本号", required = false)
    private Long resourceVersion;
    @Deprecated
    @ApiModelProperty("目录属性（dl.resource.open.scope）（1全市共享、2部门私有、3个人私有）")
    private Long openScope;
    @Deprecated
    @ApiModelProperty("目录属性（dl.resource.open.scope）（1全市共享、2部门私有、3个人私有）名称")
    private Long openScopeName;
    @ApiModelProperty(value = "资源目录类别（dl.resource.resource.type）（1数据库、2文本、3图片、4音频、5视频）", required = true)
    @Column(name = "RESOURCE_TYPE")
    private Long resourceType;
    @ApiModelProperty("资源目录类别（dl.resource.resource.type）（1数据库、2文本、3图片、4音频、5视频）名称")
    private String resourceTypeName;

    @ApiModelProperty(value = "是否系统默认通过", hidden = true)
    private Long isDefaultAc;

    @ApiModelProperty(value = "申请人账号", hidden = false)
    private String applicantAccount;
    @ApiModelProperty(value = "申请人UUID", hidden = true)
    private String applicantUuid;
    @ApiModelProperty(value = "申请人全名")
    private String applicantFullname;
    @ApiModelProperty(value = "申请人所属部门ID", hidden = false)
    private String applicantDeptId;
    @ApiModelProperty(value = "申请人所属部门Code", hidden = false)
    private String applicantDeptCode;
    @ApiModelProperty(value = "申请人部门名称")
    private String applicantDeptName;
    @ApiModelProperty(value = "申请人顶级部门ID", hidden = false)
    private String applicantDeptTopId;
    @ApiModelProperty(value = "申请人顶级部门Code", hidden = false)
    private String applicantDeptTopCode;
    @ApiModelProperty(value = "申请人顶级部门名称")
    private String applicantDeptTopName;

    @ApiModelProperty("申请时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime;
    @ApiModelProperty("申请原因")
    private String applyReason;

    @ApiModelProperty(value = "审批人UUID", hidden = true)
    private String approverUuid;
    @ApiModelProperty(value = "审批人账号", hidden = false)
    private String approverAccount;
    @ApiModelProperty("审批人全名")
    private String approverFullname;
    @ApiModelProperty(value = "审批人所属部门ID", hidden = false)
    private String approverDeptId;
    @ApiModelProperty(value = "审批人所属部门CODE", hidden = false)
    private String approverDeptCode;
    @ApiModelProperty("审批人部门名称")
    private String approverDeptName;
    @ApiModelProperty(value = "审批人顶级部门ID", hidden = false)
    private String approverDeptTopId;
    @ApiModelProperty(value = "审批人顶级部门Code", hidden = false)
    private String approverDeptTopCode;
    @ApiModelProperty(value = "审批人顶级部门名称", hidden = false)
    private String approverDeptTopName;

    @ApiModelProperty("审批时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;
    @ApiModelProperty(value = "订阅工单审批意见", required = true)
    private String approvalOpinion;

    @ApiModelProperty("审批状态(dl.approval.state)（1已审批，2未审批, 3审批不通过）")
    private Long approvalState;
    @ApiModelProperty("审批状态(dl.approval.state)（1已审批，2未审批, 3审批不通过）名称")
    private String approvalStateName;

//    @Deprecated
    @ApiModelProperty("流程状态")
    private Long flowStatus;
//    @Deprecated
    @ApiModelProperty("流程状态")
    private String flowStatusName;
    @ApiModelProperty("任务状态(1：未处理 2：处理中 3：已处理)")
    private Long taskStatus;
    @ApiModelProperty("任务状态(1：未处理 2：处理中 3：已处理) 名称")
    private String taskStatusName;

    @ApiModelProperty(value = "是否最新一次订阅", hidden = true)
    private Long isLast;

    @ApiModelProperty(value = "国信平台应用ID")
    private String gxAppId;
    @ApiModelProperty(value = "国信平台应用名称")
    private String gxAppName;

    public SubscribeTaskOrderVo(Long subscribeFormId, Long flowId, Long taskId, Long resourceId, String resourceCode, String workNum,
                                String resourceDeptId, String resourceDeptName, String resBsOwnerAccount, String resourceTableName,
                                String resourceName, Long resourceVersion, Long resourceType,
                                Long isDefaultAc,
                                String applicantAccount, String applicantUuid, String applicantFullname, String applicantDeptId,
                                String applicantDeptCode, String applicantDeptName, String applicantDeptTopId, String applicantDeptTopCode,
                                String applicantDeptTopName, Date applyTime, String applyReason,
                                String approverUuid, String approverAccount, String approverFullname, String approverDeptId,
                                String approverDeptCode, String approverDeptName, String approverDeptTopId, String approverDeptTopCode,
                                String approverDeptTopName, Date approvalTime, String approvalOpinion, Long approvalState,
                                Long flowStatus, Long taskStatus, Long isLast, String gxAppId, String gxAppName) {
        this.subscribeFormId = subscribeFormId;
        this.flowId = flowId;
        this.taskId = taskId;
        this.resourceId = resourceId;
        this.resourceCode = resourceCode;
        this.workNum = workNum;
        this.resourceDeptId = resourceDeptId;
        this.resourceDeptName = resourceDeptName;
        this.resBsOwnerAccount = resBsOwnerAccount;
        this.resourceTableName = resourceTableName;
        this.resourceName = resourceName;
        this.resourceVersion = resourceVersion;
        this.resourceType = resourceType;
        this.isDefaultAc = isDefaultAc;
        this.applicantAccount = applicantAccount;
        this.applicantUuid = applicantUuid;
        this.applicantFullname = applicantFullname;
        this.applicantDeptId = applicantDeptId;
        this.applicantDeptCode = applicantDeptCode;
        this.applicantDeptName = applicantDeptName;
        this.applicantDeptTopId = applicantDeptTopId;
        this.applicantDeptTopCode = applicantDeptTopCode;
        this.applicantDeptTopName = applicantDeptTopName;
        this.applyTime = applyTime;
        this.applyReason = applyReason;
        this.approverUuid = approverUuid;
        this.approverAccount = approverAccount;
        this.approverFullname = approverFullname;
        this.approverDeptId = approverDeptId;
        this.approverDeptCode = approverDeptCode;
        this.approverDeptName = approverDeptName;
        this.approverDeptTopId = approverDeptTopId;
        this.approverDeptTopCode = approverDeptTopCode;
        this.approverDeptTopName = approverDeptTopName;
        this.approvalTime = approvalTime;
        this.approvalOpinion = approvalOpinion;
        this.approvalState = approvalState;
        this.flowStatus = flowStatus;
        this.taskStatus = taskStatus;
        this.isLast = isLast;
        this.gxAppId = gxAppId;
        this.gxAppName = gxAppName;
    }
    public SubscribeTaskOrderVo(Long subscribeFormId, Long flowId, Long taskId, Long resourceId, String resourceCode, String workNum,
                                String resourceDeptId, String resourceDeptName, String resBsOwnerAccount, String resourceTableName,
                                String resourceName, Long resourceVersion, Long resourceType,
                                Long isDefaultAc,
                                String applicantAccount, String applicantUuid, String applicantFullname, String applicantDeptId,
                                String applicantDeptCode, String applicantDeptName, String applicantDeptTopId, String applicantDeptTopCode,
                                String applicantDeptTopName, Date applyTime, String applyReason,
                                String approverUuid, String approverAccount, String approverFullname, String approverDeptId,
                                String approverDeptCode, String approverDeptName, String approverDeptTopId, String approverDeptTopCode,
                                String approverDeptTopName, Date approvalTime, String approvalOpinion, Long approvalState,
                                Long flowStatus, String flowStatusName, Long taskStatus, Long isLast, String gxAppId, String gxAppName) {
        this.subscribeFormId = subscribeFormId;
        this.flowId = flowId;
        this.taskId = taskId;
        this.resourceId = resourceId;
        this.resourceCode = resourceCode;
        this.workNum = workNum;
        this.resourceDeptId = resourceDeptId;
        this.resourceDeptName = resourceDeptName;
        this.resBsOwnerAccount = resBsOwnerAccount;
        this.resourceTableName = resourceTableName;
        this.resourceName = resourceName;
        this.resourceVersion = resourceVersion;
        this.resourceType = resourceType;
        this.isDefaultAc = isDefaultAc;
        this.applicantAccount = applicantAccount;
        this.applicantUuid = applicantUuid;
        this.applicantFullname = applicantFullname;
        this.applicantDeptId = applicantDeptId;
        this.applicantDeptCode = applicantDeptCode;
        this.applicantDeptName = applicantDeptName;
        this.applicantDeptTopId = applicantDeptTopId;
        this.applicantDeptTopCode = applicantDeptTopCode;
        this.applicantDeptTopName = applicantDeptTopName;
        this.applyTime = applyTime;
        this.applyReason = applyReason;
        this.approverUuid = approverUuid;
        this.approverAccount = approverAccount;
        this.approverFullname = approverFullname;
        this.approverDeptId = approverDeptId;
        this.approverDeptCode = approverDeptCode;
        this.approverDeptName = approverDeptName;
        this.approverDeptTopId = approverDeptTopId;
        this.approverDeptTopCode = approverDeptTopCode;
        this.approverDeptTopName = approverDeptTopName;
        this.approvalTime = approvalTime;
        this.approvalOpinion = approvalOpinion;
        this.approvalState = approvalState;
        this.flowStatus = flowStatus;
        this.flowStatusName = flowStatusName;
        this.taskStatus = taskStatus;
        this.isLast = isLast;
        this.gxAppId = gxAppId;
        this.gxAppName = gxAppName;
    }
}
