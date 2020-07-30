package com.openjava.datalake.push.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author ljc
 *
 */
@ApiModel("推送接口管理")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_PUSH")
public class DlApiPush implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	// 使用全局ID，不依赖oracle的自增序列
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
//	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "API_ID")
	private Long apiId;
	
	@ApiModelProperty("接口唯一编号")
	@Length(min=0, max=128)
	@Column(name = "API_CODE")
	private String apiCode;
	
	@ApiModelProperty("接口名称")
	@Length(min=0, max=128)
	@Column(name = "API_NAME")
	private String apiName;

	@ApiModelProperty("接口类型(0-PUT 1-PATCH 2 DEL)")
	@Max(9L)
	@Column(name = "API_TYPE")
	private Long apiType;
	@ApiModelProperty("接口类型(0-PUT 1-PATCH 2 DEL)")
	@Transient
	private String apiTypeName;

	@ApiModelProperty("数据表名")
	@Length(min=0, max=128)
	@Column(name = "RESOURCE_TABLE_NAME")
	private String resourceTableName;
	
	@ApiModelProperty("资源目录ID")
	@Max(999999999999999999L)
	@NotNull
	@Column(name = "RESOURCE_ID")
	private Long resourceId;
	
	@ApiModelProperty("资源目录名称")
	@Length(min=0, max=256)
	@Column(name = "RESOURCE_NAME")
	private String resourceName;
	
	@ApiModelProperty("是否生成接口(0-是 1-否)")
//	@Max(9L)
	@Column(name = "IS_CREATE_API")
	private Long isCreateApi;
	@ApiModelProperty("是否生成接口(0-是 1-否)名称")
	@Transient
	private String isCreateApiName;
	
	@ApiModelProperty("接口地址")
	@Length(min=0, max=256)
	@Column(name = "API_URL")
	private String apiUrl;
	
	@ApiModelProperty("接口状态(0-有效 1-无效)")
//	@Max(9L)
	@Column(name = "API_STATE")
	private Long apiState;
	@ApiModelProperty("接口状态(0-有效 1-无效)名称")
	@Transient
	private String apiStateName;
	
	@ApiModelProperty(value = "是否启用(0-启用 1-禁用)-弃用，不再维护这个字段", hidden = true)
	@Max(9L)
	@Column(name = "IS_ENABLE")
	@Deprecated
	private Long isEnable;
	@ApiModelProperty(value = "是否启用(0-启用 1-禁用)-弃用，不再维护这个字段", hidden = true)
	@Transient
	@Deprecated
	private String isEnableName;
	
	@ApiModelProperty("创建人ID")
	@Max(999999999999999999L)
	@Column(name = "CREATOR_ID")
	private Long creatorId;

	@ApiModelProperty("创建人账号")
	private String createAccount;
	@ApiModelProperty("更新人账号")
	private String updateAccount;
	
	@ApiModelProperty("创建人姓名")
	@Length(min=0, max=128)
	@Column(name = "CREATOR_NAME")
	private String creatorName;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("接口描述")
	@Length(min=0, max=256)
	@Column(name = "REMARK")
	private String remark;
	
	@ApiModelProperty("最近更新人ID")
	@Max(999999999999999999L)
	@Column(name = "UPDATE_ID")
	private Long updateId;
	
	@ApiModelProperty("最近更新人姓名")
	@Length(min=0, max=128)
	@Column(name = "UPDATE_NAME")
	private String updateName;
	
	@ApiModelProperty("最近更新时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	private Date updateTime;

	@ApiModelProperty("上一次更新人ID")
	@Max(999999999999999999L)
	@Column(name = "LATELY_UPDATE_ID")
	private Long latelyUpdateId;

	@ApiModelProperty("上一次更新人姓名")
	@Length(min=0, max=128)
	@Column(name = "LATELY_UPDATE_NAME")
	private String latelyUpdateName;

	@ApiModelProperty("上一次更新时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LATELY_UPDATE_TIME")
	private Date latelyUpdateTime;

	@ApiModelProperty("请求示例")
	@Transient
	private String requestExample;

	@ApiModelProperty("返回示例")
	@Transient
	private String responseExample;

	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.apiId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.apiId != null) {
    		return false;
    	}
    	return true;
    }
    
}