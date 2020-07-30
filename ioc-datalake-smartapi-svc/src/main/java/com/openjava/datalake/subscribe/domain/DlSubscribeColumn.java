package com.openjava.datalake.subscribe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 实体
 * @author xjd
 *
 */
@ApiModel("订阅目录字段表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_SUBSCRIBE_COLUMN")
public class DlSubscribeColumn implements Persistable<Long>,Serializable {
	
	@ApiModelProperty(value = "订阅目录字段表ID", required = true)
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
//	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "SUBSCRIBE_COLUMN_ID")
	private Long subscribeColumnId;
	
	@ApiModelProperty("订阅申请表ID")
	@Max(999999999999999999L)
	@Column(name = "SUBSCRIBE_FORM_ID")
	private Long subscribeFormId;
	
	@ApiModelProperty(value = "资源目录结构表ID", required = true)
	@Max(999999999999999999L)
	@Column(name = "STRUCTURE_ID")
	private Long structureId;
	
	@ApiModelProperty(value = "请求字段名", required = true)
//	@Length(min=0, max=50)
	@Column(name = "STRUCTURE_NAME")
	private String structureName;
	
	@ApiModelProperty(value = "字段标识", required = true)
//	@Length(min=0, max=32)
	@Column(name = "RESOURCE_COLUM_CODE")
	private String resourceColumCode;

	/**
	 * 二期不用字段公开属性了，公开属性跟资源目录
	 */
	@Deprecated
	@ApiModelProperty(value = "字段公开属性（dl.column.open.scope）（1对外公开、2对内公开、3不公开）", required = true)
	@Max(9L)
	@Column(name = "COLUMN_VIEWABLE_TYPE")
	private Long columnViewableType;
	@Deprecated
	@ApiModelProperty("字段公开属性（dl.column.open.scope）（1对外公开、2对内公开、3不公开）名称")
	@Transient
	private String columnViewableTypeName;

	@ApiModelProperty("数据类型（dl.column.datatype）（1短字符、2较长字符、3长字符、4日期型、5整数型、6小数型）")
	private Long dataType;
	@ApiModelProperty("数据类型（dl.column.datatype）（1短字符、2较长字符、3长字符、4日期型、5整数型、6小数型）")
	@Transient
	private String dataTypeName;

	@ApiModelProperty("是否主键（1是、0否）")
	private Long isPrimaryKey;
	@ApiModelProperty("是否主键（1是、0否）")
	@Transient
	private String isPrimaryKeyName;

	@ApiModelProperty("是否申请使用（1是0否）")
	@Transient
	private Long applyUse;

	@ApiModelProperty(value = "加密状态(dl.encryption.state)（2申请不加密，1加密，0不加密）", required = true)
	@Max(9L)
	@Column(name = "ENCRYPTION_STATE")
//	@NotNull(message = "加密状态不能为空")
	private Long encryptionState;
	@ApiModelProperty("加密状态(dl.encryption.state)（2申请不加密，1加密，0不加密）名称")
	@Transient
	private String encryptionStateName;

	@ApiModelProperty("加密说明")
	private String encryptDescription;

	@ApiModelProperty("脱敏说明")
	private String insensitivesRuleDescription;

	@ApiModelProperty(value = "脱敏状态(dl.insensitives.state)（2申请不脱敏，1脱敏，0不脱敏）", required = true)
	@Max(9L)
//	@NotNull(message = "脱敏状态不能为空")
	@Column(name = "INSENSITIVES_STATE")
	private Long insensitivesState;
	@ApiModelProperty("脱敏状态(dl.insensitives.state)（2申请不脱敏，1脱敏，0不脱敏）名称")
	@Transient
	private String insensitivesStateName;
	
	@ApiModelProperty("审批字段使用权限(dl.public.permission)（1通过，0不通过, 审批后不能空，有记录就代表有申请）")
	@Max(2L)
	@Column(name = "IS_APPROVE_VIEWABLE")
	private Long isApproveViewable;
	@ApiModelProperty("审批字段使用权限(dl.public.permission)（1通过，0不通过）名称")
	@Transient
	private String isApproveViewableName;
	
	@ApiModelProperty("审批加密权限(dl.public.permission)（1通过，0不通过, NULL没申请不用审核）")
	@Max(2L)
	@Column(name = "IS_APPROVE_DECRYPTION")
	private Long isApproveDecryption;
	@ApiModelProperty("审批加密权限(dl.public.permission)（1通过，0不通过）名称")
	@Transient
	private String isApproveDecryptionName;
	
	@ApiModelProperty("审批脱敏权限(dl.public.permission)（1通过，0不通过, NULL没申请不用审核）")
	@Max(2L)
	@Column(name = "IS_APPROVE_SENSITIVE")
	private Long isApproveSensitive;
	@ApiModelProperty("审批脱敏权限(dl.public.permission)（1通过，0不通过）名称")
	@Transient
	private String isApproveSensitiveName;

	@ApiModelProperty("资源目录ID")
	private Long resourceId;
	@ApiModelProperty("申请人账号")
	private String applicantAccount;
	@ApiModelProperty("是否最新一次申请")
	private Long isLastApply;
	@ApiModelProperty("审核状态")
	private Long approvalState;
	@ApiModelProperty("审核状态(dl.structure.approval.state)（1通过申请，2待审批, 3审批不通过）名称")
	@Transient
	private String approvalStateName;
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.subscribeColumnId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.subscribeColumnId != null) {
    		return false;
    	}
    	return true;
    }

    public static DlSubscribeColumn converFromDlRescataColumn(DlRescataColumn dlRescataColumn) {
		DlSubscribeColumn dlSubscribeColumn = new DlSubscribeColumn();
		dlSubscribeColumn.setStructureName(dlRescataColumn.getColumnName());
		dlSubscribeColumn.setStructureId(dlRescataColumn.getStructureId());
		dlSubscribeColumn.setColumnViewableType(dlRescataColumn.getColumnOpenScope());
		dlSubscribeColumn.setInsensitivesRuleDescription(dlRescataColumn.getInsensitivesRuleDescription());
		dlSubscribeColumn.setEncryptDescription(dlRescataColumn.getEncryptDescription());
		dlSubscribeColumn.setDataType(dlRescataColumn.getDataType());
		dlSubscribeColumn.setIsPrimaryKey(dlRescataColumn.getIsPrimaryKey());
		dlSubscribeColumn.setEncryptionState(dlRescataColumn.getIsEncrypt());
		dlSubscribeColumn.setInsensitivesState(dlRescataColumn.getIsDesensitization());
		dlSubscribeColumn.setResourceColumCode(dlRescataColumn.getColumnDefinition());
		return dlSubscribeColumn;
	}

    public DlSubscribeColumn() {
    }

	public DlSubscribeColumn(Long subscribeColumnId, Long subscribeFormId,
                             Long structureId, String structureName, String resourceColumCode,
                             @Max(9) Long columnViewableType, Long dataType, Long isPrimaryKey,
                             @Max(9) Long encryptionState, String encryptDescription, String insensitivesRuleDescription,
                             @Max(9) Long insensitivesState, @Max(2) Long isApproveViewable, @Max(2) Long isApproveDecryption,
                             @Max(2) Long isApproveSensitive, Long resourceId, String applicantAccount, Long isLastApply,
                             Long approvalState ) {
		super();
		this.subscribeColumnId = subscribeColumnId;
		this.subscribeFormId = subscribeFormId;
		this.structureId = structureId;
		this.structureName = structureName;
		this.resourceColumCode = resourceColumCode;
		this.columnViewableType = columnViewableType;
		this.dataType = dataType;
		this.isPrimaryKey = isPrimaryKey;
		this.encryptionState = encryptionState;
		this.encryptDescription = encryptDescription;
		this.insensitivesRuleDescription = insensitivesRuleDescription;
		this.insensitivesState = insensitivesState;
		this.isApproveViewable = isApproveViewable;
		this.isApproveDecryption = isApproveDecryption;
		this.isApproveSensitive = isApproveSensitive;
		this.resourceId = resourceId;
		this.applicantAccount = applicantAccount;
		this.isLastApply = isLastApply;
		this.approvalState = approvalState;
	}

}