package com.openjava.datalake.smartapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.datalake.external.vo.SmartApiReqParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author zjf
 *
 */
@ApiModel("DL_API_REQUEST")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_REQUEST")
@NoArgsConstructor
@AllArgsConstructor
public class DlApiRequest implements Persistable<Long>,Serializable, Cloneable {

	public static final String PARAM_PAGE_NUM = "pageNum";
	public static final String PARAM_PAGE_SIZE = "pageSize";

	@ApiModelProperty("请求参数ID")
	@Id
	@Column(name = "REQUESTID")
	private Long requestId;
	
	@ApiModelProperty("查询表ID")
	@Column(name = "QUERYID")
	private Long queryId;
	
	@ApiModelProperty("字段id")
	@Column(name = "COLUMNID")
	private String columnId;

	@ApiModelProperty("字段Code")
	@Column(name = "COLUMN_CODE")
	private Long columnCode;
	
	@ApiModelProperty("字段名")
	@Length(min=0, max=200)
	@Column(name = "COLUMN_DEFINITION")
	private String columnDefinition;
	
	@ApiModelProperty("字段中文名")
	@Length(min=0, max=200)
	@Column(name = "COLUMN_NAME")
	private String columnName;
	
	@ApiModelProperty("字段类型")
	@Length(min=0, max=200)
	@Column(name = "COLUMN_TYPE")
	private String columnType;
	
	@ApiModelProperty("字段描述")
	@Length(min=0, max=2000)
	@Column(name = "COLUMN_DESC")
	private String columnDesc;
	
	@ApiModelProperty("示例值")
	@Length(min=0, max=200)
	@Column(name = "EXAMPLE_VALUE")
	private String exampleValue;
	
	@ApiModelProperty("默认值")
	@Length(min=0, max=200)
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	
	@ApiModelProperty("是否必填：0非必填、1必填")
	@Max(2L)
	@Column(name = "REQUIRED_MARK")
	private Long requiredMark;
	
	@ApiModelProperty("是否模糊搜索：0精确搜索、1模糊搜索")
	@Max(2L)
	@Column(name = "FUZZY_MARK")
	private Long fuzzyMark;
	
	@ApiModelProperty("创建人")
	@Column(name = "CREATEID")
	private String createId;
	
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
	
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.requestId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.requestId != null) {
    		return false;
    	}
    	return true;
    }

	public DlApiRequest(Long requestId, Long queryId, String columnId, @Length(min = 0, max = 200) String columnDefinition, @Length(min = 0, max = 200) String columnName, @Length(min = 0, max = 200) String columnType, @Length(min = 0, max = 2000) String columnDesc, @Length(min = 0, max = 200) String exampleValue, @Length(min = 0, max = 200) String defaultValue, @Max(2L) Long requiredMark, @Max(2L) Long fuzzyMark, String createId, @Length(min = 0, max = 100) String createBy, Date createDate, Boolean isNew) {
		this.requestId = requestId;
		this.queryId = queryId;
		this.columnId = columnId;
		this.columnDefinition = columnDefinition;
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnDesc = columnDesc;
		this.exampleValue = exampleValue;
		this.defaultValue = defaultValue;
		this.requiredMark = requiredMark;
		this.fuzzyMark = fuzzyMark;
		this.createId = createId;
		this.createBy = createBy;
		this.createDate = createDate;
		this.isNew = isNew;
	}

	@Override
	public DlApiRequest clone(){
		DlApiRequest clone = null;
		try {
			clone = (DlApiRequest) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	public SmartApiReqParamVo converToSmartApiReqParamVo() {
		SmartApiReqParamVo smartApiReqParamVo = new SmartApiReqParamVo();
		BeanUtils.copyProperties(this, smartApiReqParamVo);
		return smartApiReqParamVo;
	}
}