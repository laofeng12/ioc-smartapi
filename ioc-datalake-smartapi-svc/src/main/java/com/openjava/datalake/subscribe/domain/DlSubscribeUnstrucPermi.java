package com.openjava.datalake.subscribe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author xjd
 * @Date 2019/12/16 20:25
 * @Version 1.0
 */
@ApiModel("资源目录非结构化信息项权限表")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "DL_SUBSCRIBE_UNSTRUC_PERMI")
@Entity
public class DlSubscribeUnstrucPermi {

    @ApiModelProperty("非结构化信息项权限表ID")
    @Id
    private Long unstructurePermissionId;
    @ApiModelProperty("非结构化信息项ID")
    private Long unstructureId;

    @ApiModelProperty("订阅申请表ID")
    private Long subscribeFormId;

    @ApiModelProperty("权限所有者委办局部门ID")
    private String ownerDeptId;

    @ApiModelProperty("权限所有者委办局顶级部门ID")
    private String ownerDeptTopId;

    @ApiModelProperty("权限所有者委办局顶级部门名称")
    private String ownerDeptTopName;
    @ApiModelProperty("权限所有者委办局部门名称")
    private String ownerDeptName;
    @ApiModelProperty("权限所有者账号")
    private String ownerAccount;

    @ApiModelProperty("权限所有者UUID")
    private String ownerUuid;

    @ApiModelProperty("资源目录ID")
    private Long resourceId;

    @ApiModelProperty(name = "资源目录编码", required = true)
    private String resourceCode;
    @ApiModelProperty("资源目录版本号")
    private Long resourceVersion;

    @ApiModelProperty("权限获取时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ApiModelProperty("预览URL")
    @Transient
    private String viewUrl;
    @ApiModelProperty("下载URL")
    @Transient
    private String downloadUrl;

}
