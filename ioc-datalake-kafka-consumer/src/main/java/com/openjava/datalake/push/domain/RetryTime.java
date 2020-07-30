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
@ApiModel("重试时间表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "RETRY_TIME")
public class RetryTime implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	@TableId(type = IdType.NONE)
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("重试规则id")
	@Max(999999999999999999L)
	@Column(name = "RETRY_RULE_ID")
	private Long retryRuleId;
	
	@ApiModelProperty("时间间隔(毫秒)")
	@Max(999999999999L)
	@Column(name = "TIMES")
	private Long times;
	
	@ApiModelProperty("当前重启次数")
	@Max(9999L)
	@Column(name = "NUMBER_OF_TIMES")
	private Long numberOfTimes;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
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