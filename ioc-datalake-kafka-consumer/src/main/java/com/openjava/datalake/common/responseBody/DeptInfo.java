package com.openjava.datalake.common.responseBody;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * @author xjd
 * @D
 *
 */
@ApiModel("组织机构")
@Data
public class DeptInfo extends BasicApiResponse implements Serializable {
	
	@ApiModelProperty("组织ID")
	private String orgid;//组织ID
	private Long demid;//DEMID
	@ApiModelProperty("组织名称")
	private String orgname;//组织名称
	@ApiModelProperty("组织描述")
	private String orgdesc;//组织描述
	@ApiModelProperty("上级组织")
	private String orgsupid;//上级组织
	@ApiModelProperty("路径")
	private String path;//路径
	private Long depth;//DEPTH
	@ApiModelProperty("组织类型")
	private String orgtype;//组织类型
	@ApiModelProperty("")
	private String orgtypeName;
	@ApiModelProperty("创建人id")
	private Long creatorid;//创建人id
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty("创建时间")
	private Date createtime;//创建时间
	@ApiModelProperty("更新人id")
	private Long updateid;//更新人id
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ApiModelProperty("更新时间")
	private Date updatetime;//更新时间
	private Long sn;//SN
	@ApiModelProperty("来源")
	private Short fromtype;//来源
	@ApiModelProperty("路径全名")
	private String orgpathname;//路径全名
	@ApiModelProperty("机构编码")
	private String deptcode;//OA-机构编码
	@ApiModelProperty("组织机构代码")
	private String orgcode;//OA-组织机构代码
	@ApiModelProperty("统一社会信息代码")
	private String usccode;//OA-统一社会信息代码

}