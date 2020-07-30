package com.openjava.datalake.rescata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实体
 * @author xjd
 *
 */
@ApiModel("资源目录权限表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_RESCATA_RESOURCE_PERMISSION")
public class DlRescataResourcePermission implements Persistable<Long>,Serializable {

	@ApiModelProperty("资源目录权限表ID")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
//	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "RESOURCE_PERMISSIONS_ID")
	private Long resourcePermissionsId;

	@ApiModelProperty("订阅申请表ID")
	@Max(999999999999999999L)
	@Column(name = "SUBSCRIBE_FORM_ID")
	private Long subscribeFormId;

	@ApiModelProperty("资源目录宽表ID")
	@Max(999999999999999999L)
	@Column(name = "RESOURCE_ID")
	private Long resourceId;

	@ApiModelProperty(name = "信息资源编码", required = true)
	@Length(min=0, max=256)
	@NotNull(message = "信息资源编码不能为空")
	@Column(name = "RESOURCE_CODE")
	private String resourceCode;

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

	@ApiModelProperty("申请人账号UUID")
	@Length(min=0, max=32)
	@Column(name = "OWNER_UUID")
	private String ownerUuid;

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
	/**
	 * 可以持久化，因为权限只增不减
	 * 新版本删除了字段的情况也支持，因为每次新版本都会继承权限，重新对继承回来的权限做计算
	 */
	@ApiModelProperty("权限级别（dl.resource.permission.level）1全部权限、2部分权限、3没有权限。")
	private Long permissionLevel;
	/**
	 * 标识当前用户对该资源目录是否至少拥有一个不加密不脱敏的字段权限，1是0否
	 * 可以持久化，因为权限只增不减
	 * 新版本删除了字段的情况也支持，因为每次新版本都会继承权限，重新对继承回来的权限做计算
	 */
	@ApiModelProperty("拥有字段的全部权限的情况（dl.resource.fullpermit.level）0一个拥有全部权限的字段都没有，1至少拥有一个字段有全部权限，2全部字段都有字段的全部权限")
	private Long fullPermitLevel;


	@ApiModelProperty("局委办顶级部门名称")
	private String ownerDeptTopName;
	@ApiModelProperty("局委办部门名称")
	private String ownerDeptName;

	@ApiModelProperty("权限状态(dl.resource.permit.state)1正常、2禁用")
	@Column(name = "PERMIT_STATE")
	private Long permitState;


	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;

	@Transient
	List<DlRescataStrucPermi> dlRescataStrucPermiList = new ArrayList<>();

	@Transient
	List<DlSubscribeUnstrucPermi> dlSubscribeUnstrucPermiList = new ArrayList<>();

	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.resourcePermissionsId;
	}

    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.resourcePermissionsId != null) {
    		return false;
    	}
    	return true;
    }

}