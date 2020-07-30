package com.openjava.datalake.subscribe.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.datalake.require.domain.DlTask;
import com.openjava.datalake.require.dto.UserInfoDTO;
import com.openjava.datalake.rescata.domain.DlRescataResourcePermission;
import com.openjava.datalake.subscribe.domain.DlSubscribeColumn;
import com.openjava.datalake.workOrder.dto.WorkOrderOperatorDTO;
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
import java.util.List;

/**
 * 实体
 * @author xjd
 *
 */
@ApiModel("订阅资源目录")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_SUBSCRIBE_CATALOG_FORM")
public class DlSubscribeCatalogForm implements Persistable<Long>,Serializable,Cloneable {
	
	@ApiModelProperty("订阅申请表ID")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
//	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "SUBSCRIBE_FORM_ID")
	private Long subscribeFormId;
	
	@ApiModelProperty(value = "工单号", required = true)
	@Length(min=0, max=18)
//	@NotNull(message = "工单号不能为空")
	@Column(name = "WORK_NUM")
	private String workNum;
	@ApiModelProperty(value = "申请人账号", hidden = true)
	@Length(min=0, max=50)
	@Column(name = "APPLICANT_ACCOUNT")
	private String applicantAccount;
	
	@ApiModelProperty(value = "申请人UUID", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPLICANT_UUID")
	private String applicantUuid;
	
	@ApiModelProperty(value = "申请人全名")
	@Length(min=0, max=127)
	@Column(name = "APPLICANT_FULLNAME")
	private String applicantFullname;
	
	@ApiModelProperty(value = "申请人所属部门ID", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPLICANT_DEPT_ID")
	private String applicantDeptId;
	@ApiModelProperty(value = "申请人所属部门Code", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPLICANT_DEPT_CODE")
	private String applicantDeptCode;
	
	@ApiModelProperty(value = "申请人部门名称")
	@Length(min=0, max=100)
	@Column(name = "APPLICANT_DEPT_NAME")
	private String applicantDeptName;
	
	@ApiModelProperty(value = "申请人顶级部门ID", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPLICANT_DEPT_TOP_ID")
	private String applicantDeptTopId;
	@ApiModelProperty(value = "申请人顶级部门Code", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPLICANT_DEPT_TOP_CODE")
	private String applicantDeptTopCode;

	@ApiModelProperty(value = "申请人顶级部门名称")
	@Length(min=0, max=100)
	@Column(name = "APPLICANT_DEPT_TOP_NAME")
	private String applicantDeptTopName;

	@ApiModelProperty("申请时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_TIME")
	private Date applyTime;
	
	@ApiModelProperty(value = "资源目录ID", required = true)
	@Max(999999999999999999L)
//	@NotNull(message = "资源目录ID不能为空")
	@Column(name = "RESOURCE_ID")
	private Long resourceId;

	@ApiModelProperty(name = "信息资源编码", required = true)
	@Length(min=0, max=256)
//	@NotNull(message = "信息资源编码不能为空")
	@Column(name = "RESOURCE_CODE")
	private String resourceCode;

	@ApiModelProperty(value = "资源目录所属部门ID", required = false)
	@Length(min=0, max=36)
//	@NotNull(message = "资源目录所属部门ID不能为空")
	@Column(name = "RESOURCE_DEPT_ID")
	private String resourceDeptId;

	@ApiModelProperty(value = "资源目录业务负责人账号", required = true)
	@Length(min=0, max=32)
//	@NotNull(message = "资源目录所属部门ID不能为空")
	@Column(name = "RES_BS_OWNER_ACCOUNT")
	private String resBsOwnerAccount;
	
	@ApiModelProperty(value = "请求订阅资源目录标识", required = false)
	@Length(min=0, max=128)
//	@NotNull(message = "请求订阅资源目录标识不能为空")
	@Column(name = "RESOURCE_TABLE_NAME")
	private String resourceTableName;
	
	@ApiModelProperty(value = "请求订阅资源目录名称", required = false)
	@Length(min=0, max=256)
//	@NotNull(message = "请求订阅资源目录名称不能为空")
	@Column(name = "RESOURCE_NAME")
	private String resourceName;
	
	@ApiModelProperty(value = "资源目录版本号", required = false)
	@Max(999999999999999999L)
//	@NotNull(message = "资源目录版本号不能为空")
	@Column(name = "RESOURCE_VERSION")
	private Long resourceVersion;

	@ApiModelProperty("目录属性（dl.resource.open.scope）（1全市共享、2部门私有、3个人私有）")
	@Transient
	private Long openScope;
	@ApiModelProperty("目录属性（dl.resource.open.scope）（1全市共享、2部门私有、3个人私有）")
	@Transient
	private Long openScopeName;
	@ApiModelProperty(value = "资源目录类别（dl.resource.resource.type）（1数据库、2文本、3图片、4音频、5视频）", required = true)
	@Max(9L)
	@NotNull(message = "资源目录类别不能为空")
	@Column(name = "RESOURCE_TYPE")
	private Long resourceType;
	@ApiModelProperty("资源目录类别（dl.resource.resource.type）（1数据库、2文本、3图片、4音频、5视频）名称")
	@Transient
	private String resourceTypeName;
	
	@ApiModelProperty(value = "是否系统默认通过", hidden = true)
	@Max(9L)
	@Column(name = "IS_DEFAULT_AC")
	private Long isDefaultAc;
	
	@ApiModelProperty("申请原因")
	@Length(min=0, max=400)
	@Column(name = "APPLY_REASON")
	private String applyReason;
	
	@ApiModelProperty(value = "审批人UUID", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPROVER_UUID")
	private String approverUuid;

	@ApiModelProperty(value = "审批人账号", hidden = true)
	@Length(min=0, max=50)
	@Column(name = "APPROVER_ACCOUNT")
	private String approverAccount;
	
	@ApiModelProperty("审批人全名")
	@Length(min=0, max=127)
	@Column(name = "APPROVER_FULLNAME")
	private String approverFullname;
	
	@ApiModelProperty(value = "审批人所属部门ID", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPROVER_DEPT_ID")
	private String approverDeptId;
	@ApiModelProperty(value = "审批人所属部门CODE", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPROVER_DEPT_CODE")
	private String approverDeptCode;
	
	@ApiModelProperty("审批人部门名称")
	@Length(min=0, max=30)
	@Column(name = "APPROVER_DEPT_NAME")
	private String approverDeptName;
	
	@ApiModelProperty(value = "审批人顶级部门ID", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPROVER_DEPT_TOP_ID")
	private String approverDeptTopId;
	@ApiModelProperty(value = "审批人顶级部门Code", hidden = true)
	@Length(min=0, max=32)
	@Column(name = "APPROVER_DEPT_TOP_CODE")
	private String approverDeptTopCode;

	@ApiModelProperty(value = "审批人顶级部门名称", hidden = true)
	@Length(min=0, max=100)
	@Column(name = "APPROVER_DEPT_TOP_NAME")
	private String approverDeptTopName;

	@ApiModelProperty("审批时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVAL_TIME")
	private Date approvalTime;
	
	@ApiModelProperty(value = "资源目录审批意见", required = true)
	@Length(min=0, max=400)
	@Column(name = "APPROVAL_OPINION")
	private String approvalOpinion;
	
	@ApiModelProperty("审批状态(dl.approval.state)（1已审批，2未审批, 3审批不通过, 4审批处理中）")
	@Max(9L)
	@Column(name = "APPROVAL_STATE")
	private Long approvalState;
	@ApiModelProperty("审批状态(dl.approval.state)（1已审批，2未审批, 3审批不通过， 4审批处理中）名称")
	@Transient
	private String approvalStateName;
	
	@ApiModelProperty(value = "是否删除", hidden = true)
	@Max(9L)
	@Column(name = "IS_DELETE")
	private Long isDelete;
	@ApiModelProperty(value = "是否最新一次订阅", hidden = true)
	private Long isLast;

	@ApiModelProperty("申请来源，0-数据湖（默认），1-国信平台")
	private Long applySource;
	@ApiModelProperty("国信平台-应用Id")
	private String gxAppId;
	@ApiModelProperty("国信平台-应用名称")
	private String gxAppName;

	@ApiModelProperty("字段列表")
	@Transient
	private List<DlSubscribeColumn> subscribeColumns;



	/**
	 * permission用于mq数据同步
	 */
	@Transient
	private DlRescataResourcePermission dlRescataResourcePermission;


	@ApiModelProperty("附证文件临时ID")
	@Transient
	private String attachFileId;

	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;

	@Transient
	private String subSite;

	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.subscribeFormId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.subscribeFormId != null) {
    		return false;
    	}
    	return true;
    }

    @Override
	public DlSubscribeCatalogForm clone() {
		try {
			return (DlSubscribeCatalogForm) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

    public WorkOrderOperatorDTO converApproverToWorkOrderOperatorDTO() {
		WorkOrderOperatorDTO workOrderOperatorDTO = new WorkOrderOperatorDTO();
		workOrderOperatorDTO.setOperatorOrgName(this.approverDeptTopName)
				.setOperatorOrgId(this.approverDeptTopId)
				.setOperatorDeptName(this.approverDeptName)
				.setOperatorDeptId(this.approverDeptId)
				.setOperatorName(this.approverFullname)
				.setOperatorAccount(this.approverAccount)
				.setOperatorUuid(this.approverUuid);
		// TODO setRoleId
		return workOrderOperatorDTO;
	}

	/**
	 * 没有setUserCode、roleAlias
	 * @return
	 */
	public UserInfoDTO converApplicantToUserInfoDTO() {
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setUserAccount(this.applicantAccount)
				.setUserId(this.applicantUuid)
				.setUserFullname(this.applicantFullname)
				.setUserDeptId(this.applicantDeptId)
				.setUserDeptName(this.applicantDeptName)
				.setUserDeptTopId(this.applicantDeptTopId)
				.setUserDeptTopName(this.applicantDeptTopName);
		return userInfoDTO;
	}

	public UserInfoDTO converApproverToUserInfoDTO() {
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setUserAccount(this.approverAccount)
				.setUserId(this.approverUuid)
				.setUserFullname(this.approverFullname)
				.setUserDeptId(this.approverDeptId)
				.setUserDeptName(this.approverDeptName)
				.setUserDeptTopId(this.approverDeptTopId)
				.setUserDeptTopName(this.approverDeptTopName);
		return userInfoDTO;
	}

	public DlTask converToTaskSender(DlTask dlTask) {
		if (dlTask == null) {
			dlTask = new DlTask();
		}
		dlTask.setSenderAccount(this.applicantAccount)
				.setSenderName(this.applicantFullname)
				.setSenderDeptName(this.applicantDeptTopName)
				.setSenderDeptCode(this.applicantDeptTopCode)
				.setSenderSectionCode(this.applicantDeptCode)
				.setSenderSectionName(this.applicantDeptName);
		try {
			dlTask.setSenderId(Long.valueOf(this.applicantUuid));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return dlTask;
	}
	public DlTask converToTaskAssignee(DlTask dlTask) {
		if (dlTask == null) {
			dlTask = new DlTask();
		}
		dlTask.setAssigneeAccount(this.approverAccount)
				.setAssigneeName(this.approverFullname)
				.setAssigneeDeptCode(this.approverDeptTopId)
				.setAssigneeDeptName(this.approverDeptTopName)
				.setAssigneeSectionCode(this.approverDeptCode)
				.setAssigneeSectionName(this.approverDeptName);
		try {
			if (this.approverUuid != null) {
				dlTask.setAssigneeId(Long.valueOf(this.approverUuid));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return dlTask;
	}
}