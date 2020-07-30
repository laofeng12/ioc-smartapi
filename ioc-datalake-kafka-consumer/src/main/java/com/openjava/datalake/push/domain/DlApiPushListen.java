package com.openjava.datalake.push.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.datalake.common.responseBody.SpDlResJwt;
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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author zmk
 *
 */
@ApiModel("接口推送监听表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_API_PUSH_LISTEN")
public class DlApiPushListen implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("主键")
	@Id
	@Column(name = "LISTEN_ID")
	private Long listenId;
	
	@ApiModelProperty("监听地址")
	@Length(min=0, max=128)
	@Column(name = "LISTEN_URL")
	private String listenUrl;
	
	@ApiModelProperty("监听接口key")
	@Length(min=0, max=64)
	@Column(name = "LISTEN_KEY")
	private String listenKey;
	
	@ApiModelProperty("监听秘钥")
	@Length(min=0, max=64)
	@Column(name = "LISTEN_SECRET")
	private String listenSecret;
	
	@ApiModelProperty("创建用户账号")
	@Length(min=0, max=128)
	@Column(name = "CREATE_ACCOUNT")
	private String createAccount;
	
	@ApiModelProperty("创建用户ID")
	@Max(9223372036854775806L)
	@Column(name = "CREATE_USER")
	private Long createUser;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("修改用户账号")
	@Length(min=0, max=128)
	@Column(name = "MODIFY_ACCOUNT")
	private String modifyAccount;
	
	@ApiModelProperty("修改用户ID")
	@Max(9223372036854775806L)
	@Column(name = "MODIFY_USER")
	private Long modifyUser;
	
	@ApiModelProperty("修改时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;
	
	@ApiModelProperty("信息资源编码")
	@Length(min=0, max=256)
	@NotNull
	@Column(name = "RESOURCE_CODE")
	private String resourceCode;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;

	@Transient
	@ApiModelProperty("秘钥信息")
	private SpDlResJwt spDlResJwt;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.listenId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.listenId != null) {
    		return false;
    	}
    	return true;
    }
    
}