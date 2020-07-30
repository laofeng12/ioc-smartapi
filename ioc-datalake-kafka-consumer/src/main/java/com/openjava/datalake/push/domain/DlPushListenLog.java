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
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author zmk
 *
 */
@ApiModel("监听事件日志记录")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_PUSH_LISTEN_LOG")
public class DlPushListenLog implements Persistable<String>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	@TableId(type = IdType.UUID)
	@Column(name = "LISTEN_LOG_ID")
	private String listenLogId;
	
	@ApiModelProperty("事件唯一序列号")
	@Column(name = "RECORD_SEQUENCES")
	private String recordSequences;

	@ApiModelProperty("事件内容，json格式")
	@Column(name = "JSON_BODY")
	private String jsonBody;

	@ApiModelProperty("状态")
	@Column(name = "STATES")
	private Long states;
	@ApiModelProperty("状态名称")
	@Transient
	private String statesName;
	
	@ApiModelProperty("业务ID")
	@Column(name = "BID")
	private String bid;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("当前重试次数")
	@Column(name = "RETRY_TIMES")
	private Long retryTimes;
	
	@ApiModelProperty("日志信息")
	@Column(name = "MESSAGE")
	private String message;

	@ApiModelProperty("重试规制ID")
	@Column(name = "RETRY_RULE_ID")
	private Long retryRuleId;

	@ApiModelProperty("更新时间")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;


	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;

	@Transient
    @JsonIgnore
    @Override
    public String getId() {
        return this.listenLogId;
	}

    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.listenLogId != null) {
    		return false;
    	}
    	return true;
    }

}