package com.openjava.datalake.common.dto;

import com.openjava.datalake.rescata.domain.DlRescataResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 注册API查询接口用户信息存放类
 * @Author xjd
 * @Date 2020/2/10 1:47
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@ApiModel("注册API查询接口用户信息存放类")
public class ApiQueryRegUserInfo {

    @ApiModelProperty(value = "接口所属的机构编码（用目录的deptCode）",hidden = true)
    private String deptCode;
    @ApiModelProperty(value = "接口所属的机构名称（用目录的deptName）",hidden = true)
    private String deptName;
    @ApiModelProperty(value = "接口所属的顶级机构编码（用目录的deptTopCode）",hidden = true)
    private String topDeptCode;
    @ApiModelProperty(value = "接口所属的顶级机构名称（用目录的deptTopName）",hidden = true)
    private String topDeptName;
    @ApiModelProperty(value = "接口的创建人账户id", hidden = true)
    private String userId;
    @ApiModelProperty(value = "接口的创建人账户账号", hidden = true)
    private String account;

    public ApiQueryRegUserInfo(DlRescataResource dlRescataResource, String apiCreateUserId, String apiCreateAccout) {
        this.deptCode = dlRescataResource.getCreateDeptCode();
        this.deptName = dlRescataResource.getCreateDeptName();
        this.topDeptCode = dlRescataResource.getCreateDeptTopCode();
        this.topDeptName = dlRescataResource.getCreateDeptTopName();
        this.userId = apiCreateUserId;
        this.account = apiCreateAccout;
    }
}
