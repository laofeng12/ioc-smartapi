package com.openjava.datalake.push.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel("kafka事件发布表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "KAFKA_EVENTPUBLISH")
public class KafkaEventpublish implements Persistable<String>,Serializable {
	
	@ApiModelProperty("发布id(UUID)")
	@Id
	@Column(name = "PUBLISH_ID")
	@TableId(type = IdType.UUID)
	private String publishId;
	
	@ApiModelProperty("事件唯一序列号")
	@Length(min=0, max=32)
	@Column(name = "RECORD_SEQUENCES")
	private String recordSequences;
	
	@ApiModelProperty("事件状态（dl.eventpublish.status：0，待发布，1:已发布）")
	@Max(99L)
	@Column(name = "STATUS")
	private Long status;
	@ApiModelProperty("事件状态（dl.eventpublish.status：0，待发布，1:已发布）名称")
	@Transient
	@TableField(exist = false)
	private String statusName;
	
	@ApiModelProperty("业务类型(dl.eventpublish.btype 1:api接口入库事件.......)")
	@Max(9999L)
	@Column(name = "BTYPE")
	private Long btype;
	@ApiModelProperty("业务类型(dl.eventpublish.btype 1:api接口入库事件.......)名称")
	@Transient
	@TableField(exist = false)
	private String btypeName;
	
	@ApiModelProperty("业务ID")
	@Length(min=0, max=32)
	@Column(name = "BID")
	private String bid;
	
	@ApiModelProperty("事件内容，json格式")
	@Length(min=0, max=-1)
	@Column(name = "JSON_BODY")
	private String jsonBody;
	
	@ApiModelProperty("事件发布时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISH_TIME")
	private Date publishTime;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("创建人")
	@Max(999999999999999999L)
	@Column(name = "CREATE_USER")
	private Long createUser;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
	@TableField(exist = false)
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public String getId() {
        return this.publishId;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.publishId != null) {
    		return false;
    	}
    	return true;
    }
    
}