package com.openjava.datalake.rescata.domain;

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
 * @author xjd
 *
 */
@ApiModel("资源目录字段权限表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_RESCATA_STRUC_PERMI")
public class DlRescataStrucPermi implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("字段权限表ID")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
//	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "COLUMN_PERMISSION_ID")
	private Long columnPermissionId;
	
	@ApiModelProperty("订阅申请表ID")
	@Max(999999999999999999L)
	@Column(name = "SUBSCRIBE_FORM_ID")
	private Long subscribeFormId;
	
	@ApiModelProperty("局委办部门ID")
	@Length(min=0, max=32)
	@Column(name = "OWNER_DEPT_ID")
	private String ownerDeptId;
	
	@ApiModelProperty("局委办顶级部门ID")
	@Length(min=0, max=32)
	@Column(name = "OWNER_DEPT_TOP_ID")
	private String ownerDeptTopId;
	
	@ApiModelProperty("申请人账号")
	@Length(min=0, max=50)
	@Column(name = "OWNER_ACCOUNT")
	private String ownerAccount;
	
	@ApiModelProperty("申请人UUID")
	@Length(min=0, max=32)
	@Column(name = "OWNER_UUID")
	private String ownerUuid;
	
	@ApiModelProperty("信息资源编码")
	@Max(999999999999999999L)
	@Column(name = "RESOURCE_ID")
	private Long resourceId;

	@ApiModelProperty(name = "信息资源编码", required = true)
	@Length(min=0, max=256)
	@NotNull(message = "信息资源编码不能为空")
	@Column(name = "RESOURCE_CODE")
	private String resourceCode;
	
	@ApiModelProperty("资源目录结构表ID")
	@Max(999999999999999999L)
	@Column(name = "STRUCTURE_ID")
	private Long structureId;
	
	@ApiModelProperty("是否不用加密")
	@Max(9L)
	@Column(name = "IS_DECRYPTION")
	private Long isDecryption;
	
	@ApiModelProperty("是否不用脱敏")
	@Max(9L)
	@Column(name = "IS_SENSITIVED")
	private Long isSensitived;
	
	@ApiModelProperty("资源目录标识")
	@Length(min=0, max=128)
	@Column(name = "RESOURCE_TABLE_NAME")
	private String resourceTableName;

	@ApiModelProperty("资源目录版本号")
	@Max(999999999999999999L)
	@Column(name = "RESOURCE_VERSION")
	private Long resourceVersion;

	@ApiModelProperty("权限获取时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@ApiModelProperty("局委办顶级部门名称")
	private String ownerDeptTopName;
	@ApiModelProperty("局委办部门名称")
	private String ownerDeptName;
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.columnPermissionId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.columnPermissionId != null) {
    		return false;
    	}
    	return true;
    }
    
}