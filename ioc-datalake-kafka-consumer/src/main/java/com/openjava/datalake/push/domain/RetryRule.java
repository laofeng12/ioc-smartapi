package com.openjava.datalake.push.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 实体
 * @author zmk
 *
 */
@ApiModel("重试规制表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "RETRY_RULE")
public class RetryRule implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("重试规则id")
	@Id
	@TableId(type = IdType.NONE)
	@Column(name = "RETRY_RULE_ID")
	private Long retryRuleId;
	
	@ApiModelProperty("是否启用")
	@Max(9999L)
	@Column(name = "ENABLE")
	private Long enable;
	@ApiModelProperty("是否启用名称")
	@Transient
	private String enableName;

	@ApiModelProperty("总重试次数")
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
        return this.retryRuleId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.retryRuleId != null) {
    		return false;
    	}
    	return true;
    }
    
}