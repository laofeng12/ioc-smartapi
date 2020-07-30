package com.openjava.datalake.smartapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.datalake.external.vo.SmartApiRespParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author zjf
 *
 */
@ApiModel("DL_API_RESPONSE")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_RESPONSE")
public class DlApiResponse implements Persistable<Long>,Serializable, Cloneable{
	
	@ApiModelProperty("响应参数id")
	@Id
	@Column(name = "RESPONSEID")
	private Long responseId;
	
	@ApiModelProperty("查询表ID")
	@Column(name = "QUERYID")
	private Long queryId;
	
	@ApiModelProperty("字段ID")
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
	
	@ApiModelProperty("排序")
	@Column(name = "SORT")
	private Long sort;
	
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
        return this.responseId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.responseId != null) {
    		return false;
    	}
    	return true;
    }

	@Override
	public DlApiResponse clone() {
		DlApiResponse clone = null;
		try {
			clone = (DlApiResponse) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	public SmartApiRespParamVo converToSmartApiReqParamVo() {
		SmartApiRespParamVo smartApiRespParamVo = new SmartApiRespParamVo();
		BeanUtils.copyProperties(this, smartApiRespParamVo);
		return smartApiRespParamVo;
	}
}