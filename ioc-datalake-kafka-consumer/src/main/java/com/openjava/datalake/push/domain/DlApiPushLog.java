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
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author xjd
 *
 */
@ApiModel("推送数据日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_PUSH_LOG")
public class DlApiPushLog implements Persistable<String>,Serializable {
	
	@ApiModelProperty("唯一批次号(recordSequence)")
	@Id
	@Column(name = "LOG_ID")
	private String recordSequence;

	@ApiModelProperty("推送唯一批次号,调用接口方提供")
	@Column(name = "PUSH_SEQUENCE")
	private String pushSequence;

	@ApiModelProperty("接口描述")
	@Length(min=0, max=256)
	@Column(name = "REMARK")
	private String remark;
	
	@ApiModelProperty("请求接口路径")
	@Length(min=0, max=128)
	@Column(name = "REQ_URL")
	private String reqUrl;
	
	@ApiModelProperty("开始时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_TIMESTAMP")
	private Date beginTimestamp;
	
	@ApiModelProperty("结束时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIMESTAMP")
	private Date endTimestamp;
	
	@ApiModelProperty("结果码")
	@Max(999999999999999999L)
	@Column(name = "RES_CODE")
	private Long resCode;
	
	@ApiModelProperty("结果描述")
	@Column(name = "RES_MESSAGE")
	private String resMessage;
	
	@ApiModelProperty("创建人ID")
	@Max(999999999999999999L)
	@Column(name = "CREATOR_ID")
	private Long creatorId;
	
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
	
	@ApiModelProperty("引用接口管理表ID")
	@Max(999999999999999999L)
	@Column(name = "PID")
	private Long pid;

	@ApiModelProperty("请求日志id")
	@Max(999999999999999999L)
	@Column(name = "REQUEST_ID")
	private Long requestId;
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public String getId() {
        return this.recordSequence;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.recordSequence != null) {
    		return false;
    	}
    	return true;
    }
    
}