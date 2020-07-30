package com.openjava.datalake.rescata.vo;

import com.openjava.datalake.rescata.domain.DlRescataResource;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author JiaHai
 * @Description 资源目录信息包装类（不包含信息项）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlRescataResourceInfoVo extends DlRescataResource implements Serializable {
    private static final Long serialVersionUID = 302227128050045618L;

    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题课、5专题库）")
    private String repositoryTypeString;
    @ApiModelProperty("数据格式（dl.resource.type）（1数据库、2文本、3图片、4音频、5视频）")
    private String resourceTypeString;
    @ApiModelProperty("数据提供方式（dl.resource.data.provide.mode）（1库表挂载、2附件上传、3接口获取、4数据归集）")
    private String sourceModeString;

    @ApiModelProperty("共享范围（dl.resource.share.scope）（1全市共享、2部门私有、3个人私有）")
    private String openScopeString;

    @ApiModelProperty("更新周期（dl.resource.update.cycle）（1实时、2每日、3每周、4每月、5每季度、6每半年、7每年、8一次性、9不提供、10其他）")
    private String updateCycleString;
    @ApiModelProperty("资源安全级别（dl.resource.security.level）（1未分级、2内部、3秘密、4机密、5绝密）")
    private String securityLevelString;

    @ApiModelProperty("是否数据下载（public.YN）（1是，0否）")
    @Column(name = "IS_DATA_DOWNLOAD")
    private String isDataDownloadString;
    @ApiModelProperty("是否数据下载审批（public.YN）（1是，0否）")
    @Column(name = "IS_DATA_DOWNLOAD_APPROVAL")
    private String isDataDownloadApprovalString;
    @ApiModelProperty("是否站内消息通知（public.YN）（1是，0否）")
    @Column(name = "IS_IN_STATION_MSG_NOTIFY")
    private String isInStationMsgNotifyString;
    @ApiModelProperty("是否短信提醒（public.YN）（1是，0否）")
    @Column(name = "IS_SMS_ALERTS")
    private String isSmsAlertsString;

    @ApiModelProperty("资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）")
    private String resourceStateString;
    @ApiModelProperty("是否为最新版（public.YN）（1是、0否）")
    private String isLatestString;

    @ApiModelProperty("数据领域（dl.resource.data.domain）")
    private String dataDomainString;

    @ApiModelProperty("目录类型使用范围")
    private Long usageScope;

    public DlRescataResourceInfoVo(Long resourceId, String resourceCode, String sourceInfoCode, Long repositoryType, String resourceName, Long resourceType, String resourceTableName, Long openScope, String resourceSource, String provideDeptId, String provideDeptName, String provideDeptTopId, String provideDeptTopName, String provideDeptFullPath, Long dataDomain, Long updateCycle, String provideTimeLimit, String businessOwnerAccount, String businessOwner, Long isSmsAlerts, Long isInStationMsgNotify, Long isShowOnMap, String resourceSummary, String keyword, Long version, Date createTime, Long resourceState, Long securityLevel, Long isLatest, Long sourceMode, Date resourceStateUpdateTime, Date disabledTime, Long createUuid, String createAccount, String createFullname, String createDeptId, String createDeptName, String createDeptTopId, String createDeptTopName) {
        super(resourceId, resourceCode, sourceInfoCode, repositoryType, resourceName, resourceType, resourceTableName, openScope, resourceSource, provideDeptId, provideDeptName, provideDeptTopId, provideDeptTopName, provideDeptFullPath, dataDomain, updateCycle, provideTimeLimit, businessOwnerAccount, businessOwner, isSmsAlerts, isInStationMsgNotify, isShowOnMap, resourceSummary, keyword, version, createTime, resourceState, securityLevel, isLatest, sourceMode, resourceStateUpdateTime, disabledTime, createUuid, createAccount, createFullname, createDeptId, createDeptName, createDeptTopId, createDeptTopName);
    }
}
