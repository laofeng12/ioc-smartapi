package com.openjava.datalake.push.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 实体
 * @author zmk
 *
 */
@ApiModel("接口推送审计信息")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_PUSH_AUDIT")
public class DlApiPushAudit implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	@Column(name = "PUSH_AUDIT_ID")
	private Long pushAuditId;
	
	@ApiModelProperty("接口唯一编号")
	@Length(min=0, max=128)
	@Column(name = "API_CODE")
	private String apiCode;

	@ApiModelProperty("事项编码")
	@Length(min=0, max=32)
	@Column(name = "ITEM_ID")
	@JSONField(name="item_id")
	private String itemId;
	
	@ApiModelProperty("实施清单项")
	@Length(min=0, max=32)
	@Column(name = "ITEM_CODE")
	@JSONField(name="item_code")
	private String itemCode;
	
	@ApiModelProperty("事项环节")
	@Length(min=0, max=32)
	@Column(name = "ITEM_SEQUENCE")
	@JSONField(name="item_sequence")
	private String itemSequence;
	
	@ApiModelProperty("发起查询的IP(或者发起源)，如经过多个服务则以填写最源头的IP")
	@Length(min=0, max=32)
	@Column(name = "TERMINAL_INFO")
	@JSONField(name="terminal_info")
	private String terminalInfo;
	
	@ApiModelProperty("源头调用时间，精确到毫秒")
	@Length(min=0, max=32)
	@Column(name = "QUERY_TIMESTAMP")
	@JSONField(name="query_timestamp")
	private String queryTimestamp;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;

	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.pushAuditId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.pushAuditId != null) {
    		return false;
    	}
    	return true;
    }
    
}