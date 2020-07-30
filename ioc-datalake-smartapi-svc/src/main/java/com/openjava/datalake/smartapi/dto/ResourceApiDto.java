package com.openjava.datalake.smartapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author ccg
 * @Date 2020/5/05 16:50
 * @Version 1.0
 */
@Data
@ApiModel("当前用户可操作的资源目录列表返回对象")
public class ResourceApiDto {

    @ApiModelProperty("资源目录宽表ID")
    private Long resourceId;

    @ApiModelProperty("信息资源编码")
    private String resourceCode;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("提供部门名称")
    private String provideDeptTopName;

    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）")
    private Long repositoryType;
    @ApiModelProperty("分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）")
    private String repositoryTypeString;

    @ApiModelProperty("资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）")
    private Long resourceState;
    @ApiModelProperty("资源状态（dl.resource.resource.state）（1正常、2未上线、3已停更、4草稿、5已禁用、6待审批）")
    private String resourceStateString;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建科室ID全路径")
    private String createDeptFullPath;
    @ApiModelProperty("创建科室ID全路径")
    private String createDeptFullPathString;
    @ApiModelProperty("创建科室ID")
    private String createDeptId;

    public ResourceApiDto(Long resourceId,String resourceCode,String resourceName,String provideDeptTopName){
        this.resourceId=resourceId;
        this.resourceCode=resourceCode;
        this.resourceName=resourceName;
        this.provideDeptTopName=provideDeptTopName;
    }

    public ResourceApiDto(Long resourceId, String resourceCode, String resourceName, String provideDeptTopName,
                          Long repositoryType, Long resourceState, Date createTime, String createDeptFullPath, String createDeptId) {
        this.resourceId = resourceId;
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.provideDeptTopName = provideDeptTopName;
        this.repositoryType = repositoryType;
        this.resourceState = resourceState;
        this.createTime = createTime;
        this.createDeptFullPath = createDeptFullPath;
        this.createDeptId = createDeptId;
    }
}
