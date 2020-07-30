package com.openjava.datalake.workOrder.dto;

import com.google.common.base.Converter;
import com.openjava.admin.user.vo.OaOrgVO;
import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datalake.common.WorkOrderConstant;
import com.openjava.datalake.common.responseBody.DeptInfo;
import com.openjava.datalake.util.GetDeptInfoUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Optional;

/**
 * @Author xjd
 * @Date 2019/10/25 17:06
 * @Version 1.0
 */
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Log4j2
@ApiModel("工单操作人信息公共对象")
public class WorkOrderOperatorDTO {


//    @NotNull
    @ApiModelProperty(value = "总工单表id", required = true)
    private Long dlWorkOrderId;

    @ApiModelProperty("操作人角色Id")
    private Long operatorRoleId;
    @ApiModelProperty("操作人角色别名")
    private String operatorRoleAlias;
    @ApiModelProperty("操作人角色")
    private String operatorRoleName;
    @ApiModelProperty("操作人所属单位id（机构id）")
    private String operatorOrgId;
    @ApiModelProperty("操作人所属单位名称")
    private String operatorOrgName;
    @ApiModelProperty("操作人所属部门id")
    private String operatorDeptId;
    @ApiModelProperty("操作人所属部门名称")
    private String operatorDeptName;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作人账号")
    private String operatorAccount;
    @ApiModelProperty("操作人UUID")
    private String operatorUuid;
    @ApiModelProperty("操作人id")
    private Long operatorId;


    public void fillUpUserInfoByOaUserVO(OaUserVO user) {
        OaOrgVO topOrg = user.getTopOrg();
        String orgid = null;
        String orgname = null;
        if (topOrg != null) {
            orgid = topOrg.getOrgid();
            orgname = topOrg.getOrgname();
        } else {
            Optional<DeptInfo> optional = GetDeptInfoUtils.httpGetDeptInfo(user.getOrgid());
            if (optional.isPresent()) {
                DeptInfo deptInfo = optional.get();
                orgid = deptInfo.getOrgid();
                orgname = deptInfo.getOrgname();
            }
        }
        Long longUserId = null;
        String userId = user.getUserId();
        if (StringUtils.isNumeric(userId)) {
            longUserId = Long.valueOf(userId);
        }
        this.setOperatorAccount(user.getUserAccount())
                .setOperatorName(user.getUserName())
                .setOperatorUuid(user.getUserId())
                .setOperatorId(longUserId)
                .setOperatorDeptId(user.getOrgid())
                .setOperatorDeptName(user.getOrgname())
                .setOperatorOrgId(orgid)
                .setOperatorOrgName(orgname)
        //TODO 设置操作人的角色信息
//                .setOperatorRoleName(user.getRoleList().get(0).getRoleName())
//                .setOperatorRoleId(Long.valueOf(user.getRoleList().get(0).getRoleId()))
        ;

//        return this;
    }



}
