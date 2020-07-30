package com.openjava.datalake.push.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 实体
 * @author zmk
 *
 */
@ApiModel("待处理事件队列")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "EVENT_PROCESS")
public class EventProcess implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("队列id")
	@Id
	@TableId(type = IdType.NONE)
	@Column(name = "PROCESS_ID")
	private Long processId;
	
	@ApiModelProperty("事件唯一序列号")
	@Length(min=0, max=32)
	@Column(name = "RECORD_SEQUENCES")
	private String recordSequences;
	
	@ApiModelProperty("事件状态（dl.eventprocess.status：0，待发布，1:已发布）")
	@Max(99L)
	@Column(name = "STATUS")
	private Long status;
	@ApiModelProperty("事件状态（dl.eventprocess.status：0，待发布，1:已发布）名称")
	@Transient
	private String statusName;
	
	@ApiModelProperty("业务类型(dl.eventprocess.btype：1:api接口入库；2：监听推送数据.......)")
	@Max(9999L)
	@Column(name = "BTYPE")
	private Long btype;
	@ApiModelProperty("业务类型(dl.eventprocess.btype：1:api接口入库；2：监听推送数据.......)名称")
	@Transient
	private String btypeName;
	
	@ApiModelProperty("业务ID")
	@Length(min=0, max=32)
	@Column(name = "BID")
	private String bid;
	
	@ApiModelProperty("事件内容，json格式")
	@Length(min=0, max=-1)
	@Column(name = "JSON_BODY")
	private String jsonBody;
	
	@ApiModelProperty("重试规则id")
	@Max(999999999999999999L)
	@Column(name = "RETRY_RULE_ID")
	private Long retryRuleId;
	
	@ApiModelProperty("是否启动重试机制")
	@Max(999999999999999999L)
	@Column(name = "RETRY")
	private Long retry;
	
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
    public Long getId() {
        return this.processId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.processId != null) {
    		return false;
    	}
    	return true;
    }
    
}