package com.openjava.datalake.insensitives.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author xjd
 *
 */
@ApiModel("脱敏规则库")
@Data
@Entity
@Table(name = "DL_INSENSITIVES_RULE")
public class DlInsensitivesRule implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("脱敏规则ID")
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
//	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "INSENSITIVES_RULE_ID")
	private Long insensitivesRuleId;
	
	@ApiModelProperty(value = "规则类型(dl.insensitives.rule.type )（1.字符集;2.规则库）", required = true)
	@Max(3L)
	@NotNull(message = "规则类型不能为空")
	@Column(name = "RULE_TYPE")
	private Long ruleType;
	@ApiModelProperty("规则类型(dl.insensitives.rule.type )（1.字符集;2.规则库）名称")
	@Transient
	private String ruleTypeName;
	
	@ApiModelProperty(value = "规则名称", required = true)
	@Length(min=0, max=50)
	@Column(name = "RULE_NAME")
	private String ruleName;
	
	@ApiModelProperty("规则描述")
	@Length(min=0, max=100)
	@Column(name = "RULE_DESC")
	private String ruleDesc;
	
	@ApiModelProperty(value = "规则类路径",required = true)
	@Length(min=0, max=100)
	@NotNull(message = "规则类路径不能为空")
	@Column(name = "RULE_CLASS_PATH")
	private String ruleClassPath;
	
	@ApiModelProperty(value = "规则类方法名",required = true)
	@Length(min=0, max=30)
	@NotNull(message = "规则类方法名径不能为空")
	@Column(name = "RULE_METHOD_NAME")
	private String ruleMethodName;
	
	@ApiModelProperty(value = "左/中/右类型", hidden = true)
	@Length(min=0, max=100)
	@Column(name = "PARAM_TYPE")
	private String paramType;
	
	@ApiModelProperty("正则表达式")
	@Length(min=0, max=100)
	@Column(name = "REG_EXP")
	private String regExp;
	
	@ApiModelProperty(value = "创建者账号", hidden = true)
	@Length(min=0, max=50)
	@Column(name = "CREATE_ACCOUNT")
	private String createAccount;
	
	@ApiModelProperty(value = "创建时间", hidden = true)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty(value = "创建者全名", hidden = true)
	@Length(min=0, max=127)
	@Column(name = "CREATE_FULLNAME")
	private String createFullname;
	
	@ApiModelProperty(value = "修改者账号", hidden = true)
	@Length(min=0, max=50)
	@Column(name = "UPDATE_ACCOUNT")
	private String updateAccount;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	
	@ApiModelProperty("修改者全名")
	@Length(min=0, max=127)
	@Column(name = "UPDATE_FULLNAME")
	private String updateFullname;
	
	@ApiModelProperty("状态(public.state)(1有效，0无效)")
	@Max(1L)
	@Column(name = "STATE")
	private Long state;
	@ApiModelProperty("状态(public.state)(1有效，0无效)名称")
	@Transient
	private String stateName;
	
	@ApiModelProperty(value = "是否删除", hidden = true)
	@Max(1L)
	@Column(name = "IS_DELETE")
	private Long isDelete;
	
	
	@ApiModelProperty("是否新增")
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.insensitivesRuleId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.insensitivesRuleId != null) {
    		return false;
    	}
    	return true;
    }
    
}