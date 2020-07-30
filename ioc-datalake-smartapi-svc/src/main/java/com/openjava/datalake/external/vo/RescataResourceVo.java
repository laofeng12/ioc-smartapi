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
 * @Date 2020/4/8 20:33
 * @Version 1.0
 */
@Data
@ApiModel("资源目录信息")
public class RescataResourceVo {

    @ApiModelProperty("资源目录宽表ID")
    private Long resourceId;

    @ApiModelProperty("信息资源编码")
    private String resourceCode;

    @ApiModelProperty("源头信息资源编码")
    private String sourceInfoCode;

    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）")
    private Long repositoryType;
    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）名称")
    private String repositoryTypeString;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("数据格式（dl.resource.type）（1数据库、2文本、3图片、4音频、5视频）")
    private Long resourceType;
    @ApiModelProperty("资源类别（dl.resource.type）（1数据库、2文本、3图片、4音频、5视频）名称")
    private String resourceTypeString;
    @ApiModelProperty("数据提供方式（dl.resource.data.provide.mode）（1库表挂载、2附件上传、3接口获取、4数据归集）")
    private Long sourceMode;
    @ApiModelProperty("数据提供方式（dl.resource.data.provide.mode）（1库表挂载、2附件上传、3接口获取、4数据归集）")
    private String sourceModeString;

    @ApiModelProperty("是否内部数据源（库表挂载使用）（public.YN）（1是、0否）")
    private Long isInternalDataSource;

//    @ApiModelProperty("目录类型ID（关联目录类型表主键）")
//    private Long catalogTypeId;
//    @ApiModelProperty("目录类型明细ID（关联目录类型明细表主键）")
//    private Long catalogTypeDetailId;

    @ApiModelProperty("共享范围（dl.resource.share.scope）（1全市共享、2部门私有、3个人私有）")
    private Long openScope;
    @ApiModelProperty("共享范围（dl.resource.share.scope）（1全市共享、2部门私有、3个人私有）名称")
    private String openScopeString;

    @ApiModelProperty("存储标识")
    private String resourceTableName;

    @ApiModelProperty("资源来源")
    private String resourceSource;

    @ApiModelProperty("更新周期（dl.resource.update.cycle）（1实时、2每日、3每周、4每月、5每季度、6每半年、7每年、8一次性、9不提供、10其他）")
    private Long updateCycle;
    @ApiModelProperty("更新周期（dl.resource.update.cycle）（1实时、2每日、3每周、4每月、5每季度、6每半年、7每年、8一次性、9不提供、10其他）名称")
    private String updateCycleString;
    @ApiModelProperty("提供时限")
    private String provideTimeLimit;

    @ApiModelProperty("资源安全级别（dl.resource.security.level）（1未分级、2内部、3秘密、4机密、5绝密）")
    private Long securityLevel;
    @ApiModelProperty("资源安全级别（dl.resource.security.level）（1未分级、2内部、3秘密、4机密、5绝密）名称")
    private String securityLevelString;

    @ApiModelProperty("数据提供机构ID")
    private String provideDeptId;
    @ApiModelProperty("数据提供机构名称")
    private String provideDeptName;
    @ApiModelProperty("数据提供部门ID")
    private String provideDeptTopId;
    @ApiModelProperty("数据提供部门名称")
    private String provideDeptTopName;
    @ApiModelProperty("数据提供科室ID全路径")
    private String provideDeptFullPath;

    @ApiModelProperty("数据提供负责人账号")
    private String businessOwnerAccount;
    @ApiModelProperty("数据提供负责人姓名")
    private String businessOwner;

//    @ApiModelProperty("是否数据下载（public.YN）（1是，0否）")
//    private Long isDataDownload;
//    @ApiModelProperty("是否数据下载审批（public.YN）（1是，0否）")
//    private Long isDataDownloadApproval;
    @ApiModelProperty("是否站内消息通知（public.YN）（1是，0否）")
    private Long isInStationMsgNotify;
    @ApiModelProperty("是否短信提醒（public.YN）（1是，0否）")
    private Long isSmsAlerts;

    @ApiModelProperty("资源摘要")
    private String resourceSummary;
    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("版本号")
    private Long version;

    @ApiModelProperty("是否为最新版（public.YN）（1是、0否）")
    private Long isLatest;

    @ApiModelProperty("资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）")
    private Long resourceState;
    @ApiModelProperty("资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）")
    private String resourceStateString;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("禁用时间")
    private Date disabledTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("资源状态变动时间")
    private Date resourceStateUpdateTime;

    @ApiModelProperty("创建人UUID")
    private Long createUuid;
    @ApiModelProperty("创建人账号")
    private String createAccount;
    @ApiModelProperty("创建人名称")
    private String createFullname;

    @ApiModelProperty("所属机构ID")
    private String createDeptId;
    @ApiModelProperty("所属机构CODE")
    private String createDeptCode;
    @ApiModelProperty("所属所属名称")
    private String createDeptName;

    @ApiModelProperty("所属部门ID")
    private String createDeptTopId;
    @ApiModelProperty("所属科室CODE")
    private String createDeptTopCode;
    @ApiModelProperty("所属部门名称")
    private String createDeptTopName;

    @ApiModelProperty("所属科室ID全路径")
    private String createDeptFullPath;

//    @ApiModelProperty("是否同意在地图上展示（public.YN）（1是、0否）")
//    private Long isShowOnMap;







}
