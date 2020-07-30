package com.openjava.datalake.rescata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author JiaHai
 * @Description 资源目录宽表
 */
@Entity
@Table(name = "DL_RESCATA_RESOURCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlRescataResource implements Serializable, Cloneable {
    private static final long serialVersionUID = -7813970958617698932L;

    @Id
    @ApiModelProperty("资源目录宽表ID")
    @Column(name = "RESOURCE_ID")
    private Long resourceId;

    @ApiModelProperty("信息资源编码")
    @Column(name = "RESOURCE_CODE")
    private String resourceCode;

    @ApiModelProperty("源头信息资源编码")
    @Column(name = "SOURCE_INFO_CODE")
    private String sourceInfoCode;

    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）")
    @Column(name = "REPOSITORY_TYPE")
    private Long repositoryType;

    @ApiModelProperty("资源名称")
    @Column(name = "RESOURCE_NAME")
    private String resourceName;

    @ApiModelProperty("数据格式（dl.resource.type）（1库表、2文件、3接口）")
    @Column(name = "RESOURCE_TYPE")
    private Long resourceType;
    @ApiModelProperty("数据提供方式（dl.resource.data.provide.mode）（1库表挂载、2附件上传、3接口获取、4数据归集）")
    @Column(name = "SOURCE_MODE")
    private Long sourceMode;

    @ApiModelProperty("是否内部数据源（库表挂载使用）（public.YN）（1是、0否）")
    @Column(name = "IS_INTERNAL_DATA_SOURCE")
    private Long isInternalDataSource;

    @ApiModelProperty("目录类型ID（关联目录类型表主键）")
    @Column(name = "CATALOG_TYPE_ID")
    private Long catalogTypeId;
    @ApiModelProperty("目录类型明细ID（关联目录类型明细表主键）")
    @Column(name = "CATALOG_TYPE_DETAIL_ID")
    private Long catalogTypeDetailId;

    @ApiModelProperty("共享范围（dl.resource.share.scope）（1全市共享、2部门私有、3个人私有）")
    @Column(name = "OPEN_SCOPE")
    private Long openScope;

    @ApiModelProperty("存储标识")
    @Column(name = "RESOURCE_TABLE_NAME")
    private String resourceTableName;

    @ApiModelProperty("资源来源")
    @Column(name = "RESOURCE_SOURCE")
    private String resourceSource;

    @ApiModelProperty("更新周期（dl.resource.update.cycle）（1实时、2每日、3每周、4每月、5每季度、6每半年、7每年、8一次性、9不提供、10其他）")
    @Column(name = "UPDATE_CYCLE")
    private Long updateCycle;
    @ApiModelProperty("提供时限")
    @Column(name = "PROVIDE_TIME_LIMIT")
    private String provideTimeLimit;

    @ApiModelProperty("资源安全级别（dl.resource.security.level）（1未分级、2内部、3秘密、4机密、5绝密）")
    @Column(name = "SECURITY_LEVEL")
    private Long securityLevel;

    @ApiModelProperty("提供科室ID")
    @Column(name = "PROVIDE_DEPT_ID")
    private String provideDeptId;

    @ApiModelProperty("提供科室CODE")
    @Column(name = "PROVIDE_DEPT_CODE")
    private String provideDeptCode;

    @ApiModelProperty("提供科室名称")
    @Column(name = "PROVIDE_DEPT_NAME")
    private String provideDeptName;

    @ApiModelProperty("提供部门ID")
    @Column(name = "PROVIDE_DEPT_TOP_ID")
    private String provideDeptTopId;

    @ApiModelProperty("提供部门CODE")
    @Column(name = "PROVIDE_DEPT_TOP_CODE")
    private String provideDeptTopCode;

    @ApiModelProperty("提供部门名称")
    @Column(name = "PROVIDE_DEPT_TOP_NAME")
    private String provideDeptTopName;

    @ApiModelProperty("提供科室ID全路径")
    @Column(name = "PROVIDE_DEPT_FULL_PATH")
    private String provideDeptFullPath;

    @ApiModelProperty("提供负责人账号")
    @Column(name = "BUSINESS_OWNER_ACCOUNT")
    private String businessOwnerAccount;
    @ApiModelProperty("提供负责人姓名")
    @Column(name = "BUSINESS_OWNER")
    private String businessOwner;

    @ApiModelProperty("是否数据下载（public.YN）（1是，0否）")
    @Column(name = "IS_DATA_DOWNLOAD")
    private Long isDataDownload;
    @ApiModelProperty("是否数据下载审批（public.YN）（1是，0否）")
    @Column(name = "IS_DATA_DOWNLOAD_APPROVAL")
    private Long isDataDownloadApproval;
    @ApiModelProperty("是否站内消息通知（public.YN）（1是，0否）")
    @Column(name = "IS_IN_STATION_MSG_NOTIFY")
    private Long isInStationMsgNotify;
    @ApiModelProperty("是否短信提醒（public.YN）（1是，0否）")
    @Column(name = "IS_SMS_ALERTS")
    private Long isSmsAlerts;

    @ApiModelProperty("资源摘要")
    @Column(name = "RESOURCE_SUMMARY")
    private String resourceSummary;
    @ApiModelProperty("关键字")
    @Column(name = "KEYWORD")
    private String keyword;

    @ApiModelProperty("版本号")
    @Column(name = "VERSION")
    private Long version;

    @ApiModelProperty("是否为最新版（public.YN）（1是、0否）")
    @Column(name = "IS_LATEST")
    private Long isLatest;

    @ApiModelProperty("资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）")
    @Column(name = "RESOURCE_STATE")
    private Long resourceState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("创建时间")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DISABLED_TIME")
    @ApiModelProperty("禁用时间")
    private Date disabledTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("资源状态变动时间")
    @Column(name = "RESOURCE_STATE_UPDATE_TIME")
    private Date resourceStateUpdateTime;

    @ApiModelProperty("创建人UUID")
    @Column(name = "CREATE_UUID")
    private Long createUuid;
    @ApiModelProperty("创建人账号")
    @Column(name = "CREATE_ACCOUNT")
    private String createAccount;
    @ApiModelProperty("创建人名称")
    @Column(name = "CREATE_FULLNAME")
    private String createFullname;

    @ApiModelProperty("创建科室ID")
    @Column(name = "CREATE_DEPT_ID")
    private String createDeptId;
    @ApiModelProperty("创建科室CODE")
    @Column(name = "CREATE_DEPT_CODE")
    private String createDeptCode;
    @ApiModelProperty("创建科室名称")
    @Column(name = "CREATE_DEPT_NAME")
    private String createDeptName;

    @ApiModelProperty("创建部门ID")
    @Column(name = "CREATE_DEPT_TOP_ID")
    private String createDeptTopId;

    @ApiModelProperty("创建部门CODE")
    @Column(name = "CREATE_DEPT_TOP_CODE")
    private String createDeptTopCode;

    @ApiModelProperty("创建部门名称")
    @Column(name = "CREATE_DEPT_TOP_NAME")
    private String createDeptTopName;

    @ApiModelProperty("创建科室ID全路径")
    @Column(name = "CREATE_DEPT_FULL_PATH")
    private String createDeptFullPath;

    @ApiModelProperty("数据同步任务信息")
    @Column(name = "SYNC_INFO")
    private String syncInfo;//数据同步任务信息，调用汇聚接口获取

    @ApiModelProperty("是否同意在地图上展示（public.YN）（1是、0否）")
    @Column(name = "IS_SHOW_ON_MAP")
    private Long isShowOnMap;

    @ApiModelProperty("数据领域（dl.resource.data.domain）")
    @Column(name = "DATA_DOMAIN")
    private Long dataDomain;

    @ApiModelProperty("共享属性（dl.resource.shared.property）(1有条件共享,2无条件共享,3不予共享)")
    @Column(name = "SHARED_PROPERTY")
    private Long sharedProperty;

    @ApiModelProperty("共享条件")
    @Column(name = "SHARED_CONDITION")
    private String sharedCondition;

    @Transient
    @ApiModelProperty("数据库类型（1-Oracle; 2-MySQL; 3-数据湖内部PostgreSQL）")
    private Long databaseType;

    @Transient
    @ApiModelProperty("数据源ID（关联汇聚平台）")
    private String datasourceId;

    public DlRescataResource(Long resourceId, String resourceCode, String sourceInfoCode, Long repositoryType,
                             String resourceName, Long resourceType, String resourceTableName, Long openScope,
                             String resourceSource, String provideDeptId, String provideDeptName,
                             String provideDeptTopId, String provideDeptTopName, String provideDeptFullPath, Long dataDomain,
                             Long updateCycle, String provideTimeLimit, String businessOwnerAccount, String businessOwner,
                             Long isSmsAlerts, Long isInStationMsgNotify, Long isShowOnMap, String resourceSummary,
                             String keyword, Long version, Date createTime,
                             Long resourceState, Long securityLevel, Long isLatest,
                             Long sourceMode, Date resourceStateUpdateTime, Date disabledTime,
                             Long createUuid, String createAccount, String createFullname, String createDeptId,
                             String createDeptName, String createDeptTopId, String createDeptTopName) {
        this.resourceId = resourceId;
        this.resourceCode = resourceCode;
        this.sourceInfoCode = sourceInfoCode;
        this.repositoryType = repositoryType;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.resourceTableName = resourceTableName;
        this.openScope = openScope;
        this.resourceSource = resourceSource;
        this.provideDeptId = provideDeptId;
        this.provideDeptName = provideDeptName;
        this.provideDeptTopId = provideDeptTopId;
        this.provideDeptTopName = provideDeptTopName;
        this.provideDeptFullPath = provideDeptFullPath;
        this.dataDomain = dataDomain;
        this.updateCycle = updateCycle;
        this.provideTimeLimit = provideTimeLimit;
        this.businessOwnerAccount = businessOwnerAccount;
        this.businessOwner = businessOwner;
        this.isSmsAlerts = isSmsAlerts;
        this.isInStationMsgNotify = isInStationMsgNotify;
        this.isShowOnMap = isShowOnMap;
        this.resourceSummary = resourceSummary;
        this.keyword = keyword;
        this.version = version;
        this.createTime = createTime;
        this.resourceState = resourceState;
        this.securityLevel = securityLevel;
        this.isLatest = isLatest;
        this.sourceMode = sourceMode;
        this.resourceStateUpdateTime = resourceStateUpdateTime;
        this.disabledTime = disabledTime;
        this.createUuid = createUuid;
        this.createAccount = createAccount;
        this.createFullname = createFullname;
        this.createDeptId = createDeptId;
        this.createDeptName = createDeptName;
        this.createDeptTopId = createDeptTopId;
        this.createDeptTopName = createDeptTopName;
    }

    @Override
    public DlRescataResource clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new AssertionError("对象DlRescataResource不允许复制");
        }
        return (DlRescataResource) clone;
    }
}
