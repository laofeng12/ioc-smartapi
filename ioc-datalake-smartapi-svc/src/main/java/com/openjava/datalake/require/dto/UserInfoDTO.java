package com.openjava.datalake.require.dto;

import com.openjava.admin.user.vo.OaOrgVO;
import com.openjava.admin.user.vo.OaUserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: lsw
 * @Date: 2020/1/9 16:36
 */
@ApiModel("用户信息")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    @ApiModelProperty("负责人ID")
    private String userId;

    @ApiModelProperty("负责人账号")
    private String userAccount;

    @ApiModelProperty("负责人名称")
    private String userFullname;

    @ApiModelProperty("负责人部门ID")
    private String userDeptTopId;

    @ApiModelProperty("负责人部门CODE")
    private String userDeptTopCode;

    @ApiModelProperty("负责人部门名称")
    private String userDeptTopName;

    @ApiModelProperty("负责人科室Id")
    private String userDeptId;

    @ApiModelProperty("负责人科室CODE")
    private String userDeptCode;

    @ApiModelProperty("负责人科室名称")
    private String userDeptName;

    @ApiModelProperty("负责人角色别名（用于鉴权）")
    private String userRoleAlias;
    @ApiModelProperty("负责人角色名称")
    private String userRoleName;

    /**
     * 填充创建信息
     *
     * @param user  用户信息
     */
    public void converByOaUserVO(OaUserVO user) {
        //获取用户所属的机构信息
        OaOrgVO topOrg = user.getTopOrg();
        if (topOrg != null) {
            //部门ID
            String orgId = topOrg.getOrgid();
            this.setUserDeptTopId(orgId);
            //部门名称
            String orgName = topOrg.getOrgname();
            this.setUserDeptTopName(orgName);

        }
        String userId = user.getUserId();
        if (StringUtils.isNumeric(userId)) {
            //用户ID
            this.setUserId(userId);
        }
        //科室ID
        this.setUserDeptId(user.getOrgid())
                //科室名称
                .setUserDeptName(user.getOrgname())
                .setUserAccount(user.getUserAccount())
                .setUserFullname(user.getUserName());
    }
}
