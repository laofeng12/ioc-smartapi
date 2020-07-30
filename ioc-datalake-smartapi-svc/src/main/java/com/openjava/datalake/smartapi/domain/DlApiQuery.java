package com.openjava.datalake.smartapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实体
 * @author zjf
 *
 */
@ApiModel("DL_API_QUERY")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_QUERY")
public class DlApiQuery implements Persistable<Long>,Serializable, Cloneable {

	@ApiModelProperty("查询ID")
	@Id
	@Column(name = "QUERYID")
	private Long queryId;

	@ApiModelProperty("接口名称")
	@Length(min=0, max=200)
	@Column(name = "QUERY_NAME")
	private String queryName;

	@ApiModelProperty("接口简拼")
	@Length(min=0, max=100)
	@Column(name = "QUERY_LOGOGRAM")
	private String queryLogogram;

	@ApiModelProperty("简拼序号(简拼重复时的序号)")
	@Max(100L)
	@Column(name = "QUERY_SERIAL")
	private Long querySerial;

	@ApiModelProperty("接口编号")
	@Length(min=0, max=100)
	@Column(name = "QUERY_CODE")
	private String queryCode;

	@ApiModelProperty("资源目录ID")
	@Column(name = "RESOURCEID")
	private Long resourceId;

	@ApiModelProperty("资源目录Code")
	@Column(name = "RESOURCE_CODE")
	private String resourceCode;

	@ApiModelProperty("资料目录名称")
	@Length(min=0, max=100)
	@Column(name = "RESOURCE_NAME")
	private String resourceName;

	@ApiModelProperty("接口描述")
	@Length(min=0, max=800)
	@Column(name = "QUERY_DESC")
	private String queryDesc;

	@ApiModelProperty("是否支持分页：0不分页、1分页")
	@Max(2L)
	@Column(name = "PAGE_MARK")
	private Long pageMark;

	@ApiModelProperty("每页查询数量")
	@Max(100000L)
	@Column(name = "PAGE_SIZE")
	private Long pageSize;

	@ApiModelProperty("默认查询数量")
	@Max(100000L)
	@Column(name = "PAGE_DEFAULT_SIZE")
	private Long pageDefaultSize;

	@ApiModelProperty("是否删除：0正常、1删除")
	@Max(2L)
	@Column(name = "DELETE_MARK")
	private Long deleteMark;

	@ApiModelProperty("是否有效：0无效、1有效")
	@Max(2L)
	@Column(name = "VALID_MARK")
	private Long validMark;

	@ApiModelProperty("接口查询地址")
	@Length(min=0, max=1000)
	@Column(name = "QUERY_URL")
	private String queryUrl;

	@ApiModelProperty("转义的Url")
	@Transient
	private String enCodeQueryUrl;

	@ApiModelProperty("创建人")
	@Column(name = "CREATEID")
	private String createId;

	@ApiModelProperty("创建人账户")
	@Column(name = "CREATE_USER_ACCOUNT")
	private String createUserAccount;

	@ApiModelProperty("创建人名称")
	@Length(min=0, max=100)
	@Column(name = "CREATEBY")
	private String createBy;

	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATE")
	private Date createDate;

	@ApiModelProperty("修改人")
	@Column(name = "MODIFYID")
	private String modifyId;

	@ApiModelProperty("修改人名称")
	@Length(min=0, max=100)
	@Column(name = "MODIFYBY")
	private String modifyBy;

	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFYDATE")
	private Date modifyDate;

	@ApiModelProperty("自定义where条件")
	@Column(name = "SELF_CONFITION_SQL")
	private String selfConditionSql;

	@ApiModelProperty("是否全参数返回")
	private Long isFullResp;

	@ApiModelProperty("请求示例")
	@Transient
	private String requestExample;

	@ApiModelProperty("返回示例")
	@Transient
	private String responseExample;

	@ApiModelProperty("是否新增")
	@Transient
	private Boolean isNew;

	@ApiModelProperty(value = "请求参数集合，必填", required = true)
	@Transient
	private List<DlApiRequest> apiRequests;//请求参数集合

	@ApiModelProperty(value = "返回参数集合，必填", required = true)
	@Transient
	private List<DlApiResponse> apiResponses;//响应参数集合

	@ApiModelProperty("数据提供方式（dl.resource.data.provide.mode）（1库表挂载、2附件上传、3接口获取、4数据归集、5第三方接口）")
	@Transient
	private Long sourceMode;

	@Transient
	@JsonIgnore
	@Override
	public Long getId() {
		return this.queryId;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isNew() {
		if(isNew != null) {
			return isNew;
		}
		if(this.queryId != null && this.queryId >0) {
			return false;
		}
		return true;
	}

	/**
	 * 注册分页参数
	 */
	@Transient
	public List<DlApiRequest> addApiRequestPage(){
		//若需要分页，则注册分页参数，该方法需要手动调用才可触发。
		List<DlApiRequest> apiRequests = new ArrayList<>();
		if (this.getPageMark() == 1) {
			List<DlApiRequest> thisApiRequests = this.getApiRequests();
			DlApiRequest pageNumReqParam = new DlApiRequest(-1L, this.getId(), "-1", "pageNum", "当前页号", "Long", "当前页号", "1", "1", 1L, 0L, "", "", new Date(), false);
			DlApiRequest pageSizeReqParam = new DlApiRequest(-2L, this.getId(), "-2", "pageSize", "页大小，即每页记录数", "Long", "页大小，即每页记录数", "500", "500", 1L, 0L, "", "", new Date(), false);
			if (CollectionUtils.isNotEmpty(thisApiRequests)) {
				for (DlApiRequest thisApiRequest : thisApiRequests) {
					String pageParam = thisApiRequest.getColumnDefinition();
					if ("pageNum".equals(pageParam)) {
						pageNumReqParam = null;
					}
					if ("pageSize".equals(pageParam)) {
						pageSizeReqParam = null;
					}
				}
			}
			if (pageNumReqParam != null) {
				apiRequests.add(pageNumReqParam);
			}
			if (pageSizeReqParam != null) {
				apiRequests.add(pageSizeReqParam);
			}
		}
		if (this.getApiRequests() == null) {
			this.apiRequests = apiRequests;
		} else {
			apiRequests.addAll(this.getApiRequests());
		}
		this.setApiRequests(apiRequests);

		return apiRequests;
	}

	@Override
	public DlApiQuery clone() {
		DlApiQuery clone = null;
		try {
			clone = (DlApiQuery) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
}