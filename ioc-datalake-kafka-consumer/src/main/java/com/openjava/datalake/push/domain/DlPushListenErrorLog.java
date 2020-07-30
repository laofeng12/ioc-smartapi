package com.openjava.datalake.push.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author zmk
 *
 */
@ApiModel("监听事件错误日志")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_PUSH_LISTEN_ERROR_LOG")
public class DlPushListenErrorLog implements Persistable<String>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	@Column(name = "ID")
	@TableId(type = IdType.UUID)
	private String id;
	
	@ApiModelProperty("监听日志记录ID")
	@Length(min=0, max=32)
	@Column(name = "LISTEN_LOG_ID")
	private String listenLogId;
	
	@ApiModelProperty("日志信息")
	@Length(min=0, max=-1)
	@Column(name = "MESSAGE")
	private String message;
	
	@ApiModelProperty("CREATE_TIME")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("当前重试次数")
	@Max(9999L)
	@Column(name = "RETRY_TIMES")
	private Long retryTimes;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public String getId() {
        return this.id;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.id != null) {
    		return false;
    	}
    	return true;
    }
    
}